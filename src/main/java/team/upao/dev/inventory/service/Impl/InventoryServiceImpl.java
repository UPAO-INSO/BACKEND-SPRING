package team.upao.dev.inventory.service.Impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import team.upao.dev.common.dto.PaginationRequestDto;
import team.upao.dev.common.dto.PaginationResponseDto;
import team.upao.dev.common.utils.PaginationUtils;
import team.upao.dev.exceptions.ResourceNotFoundException;
import team.upao.dev.inventory.dto.InventoryRequestDto;
import team.upao.dev.inventory.dto.InventoryResponseDto;
import team.upao.dev.inventory.dto.InventoryFilterDto;
import team.upao.dev.inventory.dto.InventoryUpdateDto;
import team.upao.dev.inventory.enums.InventoryType;
import team.upao.dev.inventory.enums.UnitOfMeasure;
import team.upao.dev.inventory.mapper.InventoryMapper;
import team.upao.dev.inventory.model.InventoryModel;
import team.upao.dev.inventory.repository.IInventoryRepository;
import team.upao.dev.inventory.service.InventoryService;
import team.upao.dev.inventory.service.InventoryValidationService;
import team.upao.dev.inventory.service.InventoryConversionService;


import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class InventoryServiceImpl implements InventoryService {

    private final IInventoryRepository inventoryRepository;
    private final InventoryMapper inventoryMapper;
    private final InventoryValidationService validationService;
    private final InventoryConversionService conversionService;

    @Override
    @Transactional
    public InventoryResponseDto create(InventoryRequestDto request) {
        log.info("Creando nuevo ítem de inventario: {}", request.getName());
        
        //Validar cantidad
        validationService.validateQuantity(request.getQuantity());
        
        //Validar unidad
        validationService.validateUnit(request.getUnitOfMeasure());

        validationService.validateTypeAndUnitConsistency(
            request.getType(),
            request.getUnitOfMeasure(),
            request.getQuantity()
        );
        
        //Validar nombre único
        boolean nameExists = inventoryRepository.findByNameIgnoreCase(request.getName()).isPresent();
        validationService.validateUniqueInventoryName(request.getName(), nameExists);
        
        InventoryModel model = inventoryMapper.toModel(request);
        InventoryModel saved = inventoryRepository.save(model);
        
        log.info("Ítem creado exitosamente - ID: {}, Name: {}", saved.getId(), saved.getName());
        return inventoryMapper.toDto(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public InventoryResponseDto getInventoryById(Long id) {
        log.info("Buscando Item con ID: {}", id);
        InventoryModel model = inventoryRepository.findById(id)
            .orElseThrow(() -> {
                log.error("Item no encontrado: {}", id);
                return new ResourceNotFoundException(
                    String.format("Item con ID %d no encontrado", id)
                );
            });
        return inventoryMapper.toDto(model);
    }

    @Override
    @Transactional(readOnly = true)
    public PaginationResponseDto<InventoryResponseDto> findAll(PaginationRequestDto requestDto, InventoryType type) {
        final Pageable pageable = PaginationUtils.getPageable(requestDto);
        final Page<InventoryModel> entities;
        
        if (type != null) {
            entities = inventoryRepository.findAllByType(pageable, type);
        } else {
            entities = inventoryRepository.findAll(pageable);
        }

        // Mapear entities a DTOs
        final List<InventoryResponseDto> inventoryResponseDtos = inventoryMapper.toDto(entities.getContent());
        
        // Retornar respuesta con metadatos de paginación
        return new PaginationResponseDto<>(
            inventoryResponseDtos,
            entities.getTotalPages(),      // Total de páginas
            entities.getTotalElements(),   // Total de elementos
            entities.getSize(),            // Elementos por página
            entities.getNumber() + 1,      // Número de página actual (1-indexed)
            entities.isEmpty()             // ¿Está vacío?
        );
    }

    @Override
    @Transactional(readOnly = true)
    public PaginationResponseDto<InventoryResponseDto> findAllByArrayTypes(PaginationRequestDto requestDto,
                                                                            List<InventoryType> types) {
        final Pageable pageable = PaginationUtils.getPageable(requestDto);
        final Page<InventoryModel> entities = inventoryRepository.findAllByTypeIn(pageable, types);
        final List<InventoryResponseDto> inventoryResponseDtos = inventoryMapper.toDto(entities.getContent());
        return new PaginationResponseDto<>(
                inventoryResponseDtos,
                entities.getTotalPages(),
                entities.getTotalElements(),
                entities.getSize(),
                entities.getNumber() + 1,
                entities.isEmpty()
        );
    }

    @Override
    @Transactional(readOnly = true)
    public PaginationResponseDto<InventoryResponseDto> searchByName(PaginationRequestDto requestDto, 
                                                                    String searchTerm) {
        log.info("Buscando inventario por nombre: '{}' - page={}", searchTerm, requestDto.getPage());
        
        final Pageable pageable = PaginationUtils.getPageable(requestDto);
        
        final Page<InventoryModel> entities = inventoryRepository.searchByName(searchTerm, pageable);
        final List<InventoryResponseDto> dtos = inventoryMapper.toDto(entities.getContent());
        
        log.debug("Búsqueda encontró {} resultados", entities.getTotalElements());
        return new PaginationResponseDto<>(
            dtos,
            entities.getTotalPages(),
            entities.getTotalElements(),
            entities.getSize(),
            entities.getNumber() + 1,
            entities.isEmpty()
        );
    }

    @Override
    @Transactional(readOnly = true)
    public PaginationResponseDto<InventoryResponseDto> findOutOfStockItems(PaginationRequestDto requestDto) {
        log.error("Buscando ítems AGOTADOS - page={}", requestDto.getPage());
        
        final Pageable pageable = PaginationUtils.getPageable(requestDto);
        
        final Page<InventoryModel> entities = inventoryRepository.findOutOfStockItems(pageable);
        final List<InventoryResponseDto> dtos = inventoryMapper.toDto(entities.getContent());
        
        if (!entities.isEmpty()) {
            log.error("ALERTA: Se encontraron {} ítems agotados", entities.getTotalElements());
        }
        
        return new PaginationResponseDto<>(
            dtos,
            entities.getTotalPages(),
            entities.getTotalElements(),
            entities.getSize(),
            entities.getNumber() + 1,
            entities.isEmpty()
        );
    }

    @Override
    @Transactional(readOnly = true)
    public PaginationResponseDto<InventoryResponseDto> findAllByNameAndType(PaginationRequestDto requestDto, 
                                                                        InventoryFilterDto filter) {
        log.info("Filtrando inventario: type={}, searchTerm={}", 
            filter.getType(), filter.getSearchTerm());
        
        final Pageable pageable = PaginationUtils.getPageable(requestDto);
        final Page<InventoryModel> entities;
        
        // Lógica de filtrado según criterios disponibles
        if (filter.getType() != null &&
            filter.getSearchTerm() != null && !filter.getSearchTerm().isEmpty()) {
            //  Ambos criterios: tipo + búsqueda
            log.debug("Aplicando filtro completo: type + nombre");
            entities = inventoryRepository.findByTypeAndName(
                filter.getType(), 
                filter.getSearchTerm(), 
                pageable
            );
        } else if (filter.getType() != null) {
            // Solo tipo
            log.debug("Filtro por tipo: {}", filter.getType());
            entities = inventoryRepository.findAllByType(pageable, filter.getType());
        } else if (filter.getSearchTerm() != null && !filter.getSearchTerm().isEmpty()) {
            //  Solo nombre
            log.debug("Filtro por nombre: {}", filter.getSearchTerm());
            entities = inventoryRepository.searchByName(filter.getSearchTerm(), pageable);
        } else {
            //  Sin filtros
            log.debug("Sin filtros, retornando todos");
            entities = inventoryRepository.findAll(pageable);
        }
        
        final List<InventoryResponseDto> dtos = inventoryMapper.toDto(entities.getContent());
        return new PaginationResponseDto<>(
            dtos,
            entities.getTotalPages(),
            entities.getTotalElements(),
            entities.getSize(),
            entities.getNumber() + 1,
            entities.isEmpty()
        );
    }

    @Override
    @Transactional
    public InventoryResponseDto update(Long id, InventoryUpdateDto request) {
        log.info("Actualizando inventario con ID: {}", id);
        
        InventoryModel existing = this.findModelById(id);

        InventoryType oldType = existing.getType();
        UnitOfMeasure oldUnit = existing.getUnitOfMeasure();
        
        // Solo actualizar si el campo tiene un valor no nulo
        if (request.getName() != null) {
            existing.setName(request.getName());
        }
        if (request.getQuantity() != null) {
            validationService.validateQuantity(request.getQuantity());
            existing.setQuantity(request.getQuantity());
            log.debug("Cantidad actualizada: {}", request.getQuantity());
        }
        if (request.getType() != null) {
            existing.setType(request.getType());
        }
        if (request.getUnitOfMeasure() != null) {
            validationService.validateUnit(request.getUnitOfMeasure());
            existing.setUnitOfMeasure(request.getUnitOfMeasure());
            log.debug("Unidad actualizada: {}", request.getUnitOfMeasure().getSymbol());
        }

        try {
            // Validar coherencia general
            validationService.validateTypeAndUnitConsistency(
                existing.getType(),
                existing.getUnitOfMeasure(),
                existing.getQuantity()
            );
            
            // Validar cambios específicos si hubo modificaciones
            if (!oldType.equals(existing.getType())) {
                validationService.validateTypeChange(
                    oldType, 
                    existing.getType(), 
                    existing.getUnitOfMeasure(), 
                    existing.getQuantity()
                );
            }
            
            if (!oldUnit.equals(existing.getUnitOfMeasure())) {
                validationService.validateUnitChange(
                    existing.getType(), 
                    oldUnit, 
                    existing.getUnitOfMeasure(), 
                    existing.getQuantity()
                );
            }
        } catch (IllegalArgumentException e) {
            log.error(" Validación de actualización fallida: {}", e.getMessage());
            throw e;
        }
        
        InventoryModel updated = inventoryRepository.save(existing);
        
        log.info("Inventario actualizado: {}", id);
        return inventoryMapper.toDto(updated);
    }

    @Override
    @Transactional
    public void deductStock(Long inventoryId, BigDecimal quantity, UnitOfMeasure unit) {
        log.info("Deduciendo {} unidades del inventario ID: {}", quantity, inventoryId);
        
        validationService.validateQuantity(quantity);
        validationService.validateUnit(unit);

        InventoryModel inventory = findModelById(inventoryId);
        
        log.debug("Inventario actual: {} {} {}", 
            inventory.getQuantity(), inventory.getUnitOfMeasure().getSymbol(), inventory.getName());

        if (!unit.isCompatible(inventory.getUnitOfMeasure())) {
            log.error("Unidades incompatibles: {} vs {}", 
                unit.getSymbol(), inventory.getUnitOfMeasure().getSymbol());
            throw new IllegalArgumentException(
                String.format(
                    "Unidad incompatible. Se intenta descontar en %s pero el inventario usa %s",
                    unit.getSymbol(), inventory.getUnitOfMeasure().getSymbol()
                )
            );
        }

        BigDecimal quantityInInventoryUnit = conversionService.convert(quantity, unit, inventory.getUnitOfMeasure());

        log.debug("Cantidad convertida: {} {} = {} {}", 
            quantity, unit.getSymbol(),
            quantityInInventoryUnit, inventory.getUnitOfMeasure().getSymbol());
        
        //Validar stock suficiente
        validationService.validateSufficientStock(inventory, quantity, unit);
        
        // ✅ Restar cantidad
        BigDecimal newQuantity = inventory.getQuantity().subtract(quantityInInventoryUnit);
        inventory.setQuantity(newQuantity);
        inventoryRepository.save(inventory);
        
        log.info("Stock deducido: {} → Stock restante: {} {}", 
            inventory.getName(), newQuantity, inventory.getUnitOfMeasure().getSymbol());
    }

    @Override
    @Transactional(readOnly = true)
    public boolean hasEnoughStock(Long inventoryId, BigDecimal quantity, UnitOfMeasure unit) {
        log.debug("Verificando stock - ID: {}, requerido: {}", inventoryId, quantity);
        
        try {
            validationService.validateQuantity(quantity);
            validationService.validateUnit(unit);
            
            InventoryModel inventory = findModelById(inventoryId);
            
            if (!unit.isCompatible(inventory.getUnitOfMeasure())) {
                log.warn("nidades incompatibles - asumiendo sin stock");
                return false;
            }
            
            BigDecimal quantityInInventoryUnit = conversionService.convert(quantity, unit, inventory.getUnitOfMeasure());
            
            boolean hasStock = inventory.getQuantity().compareTo(quantityInInventoryUnit) >= 0;
            
            log.debug("✅ Verificación: {} → Stock: {} {}", 
                hasStock ? "Suficiente" : "Insuficiente",
                inventory.getQuantity(), inventory.getUnitOfMeasure().getSymbol());
            
            return hasStock;
        } catch (Exception e) {
            log.error("Error al verificar stock: {}", e.getMessage());
            return false;
        }
    }

    @Override
    @Transactional(readOnly = true)
    public InventoryModel findModelById(Long id) {
        return inventoryRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException(
                String.format("Inventario con ID %d no encontrado", id)
            ));
    }

    @Override
    @Transactional
    public void delete(Long id) {
        log.info("Eliminando inventario con ID: {}", id);
        
        if (!inventoryRepository.existsById(id)) {
            log.error("Inventario no encontrado para eliminar: {}", id);
            throw new ResourceNotFoundException(
                String.format("Inventario con ID %d no encontrado", id)
            );
        }
        
        inventoryRepository.deleteById(id);
        log.info("Inventario eliminado: {}", id);
    }

}