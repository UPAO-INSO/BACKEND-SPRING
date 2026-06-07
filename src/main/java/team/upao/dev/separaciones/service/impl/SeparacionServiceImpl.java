package team.upao.dev.separaciones.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import team.upao.dev.customers.service.CustomerService;
import team.upao.dev.exceptions.ResourceNotFoundException;
import team.upao.dev.inventory.dto.ProductInventoryResponseDto;
import team.upao.dev.inventory.service.InventoryService;
import team.upao.dev.inventory.service.ProductInventoryService;
import team.upao.dev.menu.model.MenuDiarioItemModel;
import team.upao.dev.menu.repository.IMenuDiarioItemRepository;
import team.upao.dev.pensionistas.model.PensionistaModel;
import team.upao.dev.pensionistas.service.PensionistaService;
import team.upao.dev.products.enums.ProductTypeEnum;
import team.upao.dev.products.model.ProductModel;
import team.upao.dev.products.service.ProductService;
import team.upao.dev.separaciones.dto.SeparacionItemRequestDto;
import team.upao.dev.separaciones.dto.SeparacionRequestDto;
import team.upao.dev.separaciones.dto.SeparacionResponseDto;
import team.upao.dev.separaciones.dto.SeparacionStatusUpdateDto;
import team.upao.dev.separaciones.enums.SeparacionStatus;
import team.upao.dev.separaciones.mapper.SeparacionMapper;
import team.upao.dev.separaciones.model.SeparacionItemModel;
import team.upao.dev.separaciones.model.SeparacionModel;
import team.upao.dev.separaciones.repository.ISeparacionRepository;
import team.upao.dev.separaciones.service.SeparacionService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class SeparacionServiceImpl implements SeparacionService {

    private final ISeparacionRepository separacionRepository;
    private final IMenuDiarioItemRepository menuDiarioItemRepository;
    private final PensionistaService pensionistaService;
    private final CustomerService customerService;
    private final ProductService productService;
    private final ProductInventoryService productInventoryService;
    private final InventoryService inventoryService;
    private final SeparacionMapper mapper;

    // ── Pricing ──────────────────────────────────────────────────────

    private double calculateRegularPrice(List<SeparacionItemModel> items) {
        BigDecimal total = BigDecimal.ZERO;
        List<BigDecimal> starterPrices = new ArrayList<>();
        int segundosUnits = 0;

        for (SeparacionItemModel item : items) {
            int qty = item.getQuantity();
            BigDecimal price = BigDecimal.valueOf(item.getProduct().getPrice());
            String typeName = Optional.ofNullable(item.getProduct().getProductType())
                    .map(t -> t.getName()).orElse("");

            if (ProductTypeEnum.SEGUNDOS.name().equalsIgnoreCase(typeName)) {
                segundosUnits += qty;
                total = total.add(price.multiply(BigDecimal.valueOf(qty)));
            } else if (ProductTypeEnum.ENTRADAS.name().equalsIgnoreCase(typeName)) {
                starterPrices.addAll(Collections.nCopies(qty, price));
            } else {
                total = total.add(price.multiply(BigDecimal.valueOf(qty)));
            }
        }

        starterPrices.sort(Comparator.reverseOrder());
        int startersToCharge = Math.max(0, starterPrices.size() - segundosUnits);
        for (int i = 0; i < startersToCharge; i++) {
            total = total.add(starterPrices.get(i));
        }

        return total.doubleValue();
    }

    // ── Create ───────────────────────────────────────────────────────

    @Override
    @Transactional
    public SeparacionResponseDto create(SeparacionRequestDto dto) {
        SeparacionModel separacion = SeparacionModel.builder()
                .date(LocalDate.now())
                .notes(dto.getNotes())
                .build();

        // Resolver pensionista
        PensionistaModel pensionista = null;
        if (dto.getPensionistaId() != null) {
            pensionista = pensionistaService.findModelById(dto.getPensionistaId());
            if (!pensionista.getActive() || pensionista.getCreditsRemaining() <= 0) {
                throw new IllegalStateException("El pensionista no tiene créditos disponibles");
            }
            separacion.setPensionista(pensionista);
        }

        // Resolver cliente
        if (dto.getCustomerId() != null) {
            separacion.setCustomer(customerService.findModelById(dto.getCustomerId()));
        } else {
            separacion.setClientName(dto.getClientName());
        }

        // Construir ítems
        List<SeparacionItemModel> items = buildItems(dto.getItems(), separacion, pensionista != null);
        separacion.setItems(items);

        // Calcular precio total
        double totalPrice = pensionista != null
                ? pensionista.getPlanPricePerMeal()
                : calculateRegularPrice(items);
        separacion.setTotalPrice(totalPrice);

        SeparacionModel saved = separacionRepository.save(separacion);

        // Reservar porciones en el menú diario
        reservePortions(dto.getItems());

        log.info("Separación creada id={} cliente={}", saved.getId(), resolveLogName(saved));
        return mapper.toDto(saved);
    }

    private List<SeparacionItemModel> buildItems(
            List<SeparacionItemRequestDto> dtos,
            SeparacionModel separacion,
            boolean isPensionista) {

        return dtos.stream().map(itemDto -> {
            ProductModel product = productService.findModelById(itemDto.getProductId());

            if (!product.getAvailable()) {
                throw new IllegalArgumentException(
                        "El producto '" + product.getName() + "' no está disponible hoy");
            }

            return SeparacionItemModel.builder()
                    .separacion(separacion)
                    .product(product)
                    .quantity(itemDto.getQuantity())
                    .unitPrice(isPensionista ? 0.0 : product.getPrice())
                    .build();
        }).toList();
    }

    private void reservePortions(List<SeparacionItemRequestDto> items) {
        LocalDate today = LocalDate.now();
        for (SeparacionItemRequestDto item : items) {
            menuDiarioItemRepository
                    .findByProductIdAndDate(item.getProductId(), today)
                    .ifPresent(mdi -> {
                        mdi.setUsedPortions(mdi.getUsedPortions() + item.getQuantity());
                        menuDiarioItemRepository.save(mdi);
                    });
        }
    }

    private void releasePortions(List<SeparacionItemModel> items) {
        LocalDate today = LocalDate.now();
        for (SeparacionItemModel item : items) {
            menuDiarioItemRepository
                    .findByProductIdAndDate(item.getProduct().getId(), today)
                    .ifPresent(mdi -> {
                        int released = Math.max(0, mdi.getUsedPortions() - item.getQuantity());
                        mdi.setUsedPortions(released);
                        menuDiarioItemRepository.save(mdi);
                    });
        }
    }

    // ── Queries ──────────────────────────────────────────────────────

    @Override
    @Transactional(readOnly = true)
    public SeparacionResponseDto findById(Long id) {
        return mapper.toDto(findModelById(id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<SeparacionResponseDto> findByDate(LocalDate date) {
        return mapper.toDto(separacionRepository.findAllByDateOrderByCreatedAtAsc(date));
    }

    @Override
    @Transactional(readOnly = true)
    public List<SeparacionResponseDto> findToday() {
        return findByDate(LocalDate.now());
    }

    // ── Status change ─────────────────────────────────────────────────

    @Override
    @Transactional
    public SeparacionResponseDto changeStatus(Long id, SeparacionStatusUpdateDto dto) {
        SeparacionModel separacion = findModelById(id);
        SeparacionStatus current = separacion.getStatus();
        SeparacionStatus next = dto.getStatus();

        validateTransition(current, next);

        if (next == SeparacionStatus.ENTREGADA) {
            deductInventory(separacion);
            if (separacion.getPensionista() != null) {
                pensionistaService.registrarConsumo(separacion.getPensionista(), separacion);
            }
        }

        if (next == SeparacionStatus.CANCELADA) {
            releasePortions(separacion.getItems());
        }

        separacion.setStatus(next);
        SeparacionModel saved = separacionRepository.save(separacion);

        log.info("Separación {} cambió de {} a {}", id, current, next);
        return mapper.toDto(saved);
    }

    @Override
    @Transactional
    public String cancel(Long id) {
        SeparacionStatusUpdateDto dto = new SeparacionStatusUpdateDto();
        dto.setStatus(SeparacionStatus.CANCELADA);
        changeStatus(id, dto);
        return "Separación " + id + " cancelada";
    }

    // ── Inventory deduction (same logic as OrderServiceImpl) ─────────

    private void deductInventory(SeparacionModel separacion) {
        for (SeparacionItemModel item : separacion.getItems()) {
            Long productId = item.getProduct().getId();
            int qty = item.getQuantity();

            if (!productInventoryService.canSellProduct(productId, BigDecimal.valueOf(qty))) {
                throw new IllegalArgumentException(
                        "Stock insuficiente para: " + item.getProduct().getName());
            }
        }

        for (SeparacionItemModel item : separacion.getItems()) {
            List<ProductInventoryResponseDto> recipe =
                    productInventoryService.getRecipeByProductId(item.getProduct().getId());

            for (ProductInventoryResponseDto ingredient : recipe) {
                BigDecimal toDeduct = ingredient.getQuantity()
                        .multiply(BigDecimal.valueOf(item.getQuantity()));

                inventoryService.deductStock(
                        ingredient.getInventoryId(),
                        toDeduct,
                        ingredient.getUnitOfMeasure());

                log.info("Inventario descontado: {} {} de {} (separación {})",
                        toDeduct, ingredient.getUnitOfMeasure(),
                        ingredient.getInventoryName(), separacion.getId());
            }
        }
    }

    // ── Helpers ──────────────────────────────────────────────────────

    private SeparacionModel findModelById(Long id) {
        return separacionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Separación no encontrada con id: " + id));
    }

    private void validateTransition(SeparacionStatus current, SeparacionStatus next) {
        if (current == SeparacionStatus.ENTREGADA || current == SeparacionStatus.CANCELADA) {
            throw new IllegalStateException(
                    "No se puede cambiar el estado de una separación " + current);
        }
        if (next == SeparacionStatus.PENDIENTE) {
            throw new IllegalStateException("No se puede volver a PENDIENTE");
        }
    }

    private String resolveLogName(SeparacionModel model) {
        if (model.getPensionista() != null) return model.getPensionista().getCustomer().getName();
        if (model.getClientName() != null) return model.getClientName();
        if (model.getCustomer() != null) return model.getCustomer().getName();
        return "desconocido";
    }
}
