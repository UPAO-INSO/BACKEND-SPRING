package team.upao.dev.inventory.service;

import java.math.BigDecimal;
import java.util.List;

import team.upao.dev.inventory.dto.ProductInventoryRequestDto;
import team.upao.dev.inventory.dto.ProductInventoryResponseDto;
import team.upao.dev.inventory.dto.ProductInventoryUpdateDto;

public interface ProductInventoryService {
    
    // =============================================
    // HU1: REGISTRAR RECETAS
    // =============================================
    /**
     * HU1: Registrar insumos utilizados en preparación de platillos
     * Asociar un insumo con un producto y su cantidad requerida
     */
    ProductInventoryResponseDto createProductInventory(ProductInventoryRequestDto request);
    
    // =============================================
    // HU4: VISUALIZAR RECETAS
    // =============================================
    /**
     * Obtener receta completa de un producto (todos sus ingredientes)
     */
    List<ProductInventoryResponseDto> getRecipeByProductId(Long productId);
    
    /**
     * Obtener detalle de una relación específica
     */
    ProductInventoryResponseDto getProductInventoryById(Long id);
    
    /**
     * Obtener todas las recetas registradas
     */
    List<ProductInventoryResponseDto> getAllProductInventories();    

    /**
     * Verificar si un producto tiene receta registrada
     */
    boolean hasRecipe(Long productId);
       
    // =============================================
    // HU3: MODIFICAR RECETAS
    // =============================================
    /**
     * Modificar cantidad de insumo en una receta
     */
    ProductInventoryResponseDto updateProductInventory(Long id, ProductInventoryUpdateDto request);
    
    // =============================================
    // HU5: VALIDACIÓN PARA VENTAS
    // =============================================
    /**
     * Verificar si se puede vender un producto (stock de todos sus ingredientes)
     */
    boolean canSellProduct(Long productId, BigDecimal quantity);
    
    /**
     * Verificar stock y retornar mensaje detallado de error si no hay suficiente
     * @return null si hay stock suficiente, mensaje de error si no
     */
    String getStockErrorDetail(Long productId, BigDecimal quantity);
    
    /**
     * Obtener ingredientes para deducir (receta de un producto)
     * Usado cuando se vende un plato
     */
    List<ProductInventoryResponseDto> getIngredientsForDeduction(Long productId);
    
    // =============================================
    // ELIMINAR
    // =============================================
    void deleteProductInventory(Long id);
}