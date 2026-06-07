package team.upao.dev.menu.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import team.upao.dev.exceptions.ResourceNotFoundException;
import team.upao.dev.menu.dto.MenuDiarioItemRequestDto;
import team.upao.dev.menu.dto.MenuDiarioItemResponseDto;
import team.upao.dev.menu.mapper.MenuDiarioMapper;
import team.upao.dev.menu.model.MenuDiarioItemModel;
import team.upao.dev.menu.repository.IMenuDiarioItemRepository;
import team.upao.dev.menu.service.MenuDiarioService;
import team.upao.dev.products.model.ProductModel;
import team.upao.dev.products.service.ProductService;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class MenuDiarioServiceImpl implements MenuDiarioService {

    private final IMenuDiarioItemRepository menuDiarioItemRepository;
    private final ProductService productService;
    private final MenuDiarioMapper mapper;

    @Override
    @Transactional
    public List<MenuDiarioItemResponseDto> saveForToday(List<MenuDiarioItemRequestDto> items) {
        LocalDate today = LocalDate.now();

        List<MenuDiarioItemModel> saved = items.stream().map(dto -> {
            ProductModel product = productService.findModelById(dto.getProductId());

            MenuDiarioItemModel model = menuDiarioItemRepository
                    .findByProductIdAndDate(dto.getProductId(), today)
                    .orElseGet(() -> MenuDiarioItemModel.builder()
                            .product(product)
                            .date(today)
                            .usedPortions(0)
                            .build());

            model.setEstimatedPortions(dto.getEstimatedPortions());
            return menuDiarioItemRepository.save(model);
        }).toList();

        log.info("Menú diario actualizado: {} productos para {}", saved.size(), today);
        return mapper.toDto(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public List<MenuDiarioItemResponseDto> findToday() {
        return findByDate(LocalDate.now());
    }

    @Override
    @Transactional(readOnly = true)
    public List<MenuDiarioItemResponseDto> findByDate(LocalDate date) {
        return mapper.toDto(menuDiarioItemRepository.findAllByDateOrderByProductId(date));
    }

    @Override
    @Transactional
    public MenuDiarioItemResponseDto updatePortions(Long id, Integer estimatedPortions) {
        MenuDiarioItemModel model = findModelById(id);

        if (estimatedPortions < model.getUsedPortions()) {
            throw new IllegalArgumentException(
                    "Las porciones estimadas (" + estimatedPortions +
                    ") no pueden ser menores a las ya usadas (" + model.getUsedPortions() + ")");
        }

        model.setEstimatedPortions(estimatedPortions);
        return mapper.toDto(menuDiarioItemRepository.save(model));
    }

    @Override
    @Transactional
    public String remove(Long id) {
        MenuDiarioItemModel model = findModelById(id);
        menuDiarioItemRepository.delete(model);
        return "Producto retirado del menú diario";
    }

    private MenuDiarioItemModel findModelById(Long id) {
        return menuDiarioItemRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Ítem de menú no encontrado con id: " + id));
    }
}
