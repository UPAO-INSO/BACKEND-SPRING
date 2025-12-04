package team.upao.dev.inventory.service;

import java.math.BigDecimal;
import java.util.List;

import team.upao.dev.common.dto.PaginationRequestDto;
import team.upao.dev.common.dto.PaginationResponseDto;
import team.upao.dev.inventory.dto.InventoryFilterDto;
import team.upao.dev.inventory.dto.InventoryRequestDto;
import team.upao.dev.inventory.dto.InventoryResponseDto;
import team.upao.dev.inventory.dto.InventoryUpdateDto;
import team.upao.dev.inventory.enums.InventoryType;
import team.upao.dev.inventory.enums.UnitOfMeasure;
import team.upao.dev.inventory.model.InventoryModel;

public interface InventoryService {
    
    //Registrar insumos y productos
    InventoryResponseDto create(InventoryRequestDto request);

    InventoryResponseDto getInventoryById(Long id); // Obtener un ítem específico por ID

    PaginationResponseDto<InventoryResponseDto> findAll(PaginationRequestDto requestDto, InventoryType type); 
    PaginationResponseDto<InventoryResponseDto> findAllByArrayTypes(PaginationRequestDto requestDto, List<InventoryType> types);
    PaginationResponseDto<InventoryResponseDto> searchByName(PaginationRequestDto requestDto, String searchTerm);
    PaginationResponseDto<InventoryResponseDto> findAllByNameAndType(PaginationRequestDto requestDto, InventoryFilterDto filter);
    PaginationResponseDto<InventoryResponseDto> findOutOfStockItems(PaginationRequestDto requestDto);

    InventoryResponseDto update(Long id, InventoryUpdateDto request);

    void deductStock(Long inventoryId, BigDecimal quantity, UnitOfMeasure unit);
    void restoreStock(Long inventoryId, BigDecimal quantity, UnitOfMeasure unit);
    boolean hasEnoughStock(Long inventoryId, BigDecimal quantity, UnitOfMeasure unit);
    InventoryModel findModelById(Long id);
    
    void delete(Long id);
}