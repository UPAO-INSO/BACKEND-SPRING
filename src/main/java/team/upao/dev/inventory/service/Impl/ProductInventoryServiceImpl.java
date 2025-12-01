package team.upao.dev.inventory.service.Impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import team.upao.dev.exceptions.NotFoundException;
import team.upao.dev.inventory.dto.ProductInventoryRequestDto;
import team.upao.dev.inventory.dto.ProductInventoryResponseDto;
import team.upao.dev.inventory.mapper.ProductInventoryMapper;
import team.upao.dev.inventory.model.InventoryModel;
import team.upao.dev.inventory.model.ProductInventoryModel;
import team.upao.dev.inventory.repository.IInventoryRepository;
import team.upao.dev.inventory.repository.IProductInventoryRepository;
import team.upao.dev.products.model.ProductModel;
import team.upao.dev.products.repository.IProductRepository;
import team.upao.dev.inventory.service.InventoryService;
import team.upao.dev.inventory.service.ProductInventoryService;

import java.math.BigDecimal;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class ProductInventoryServiceImpl implements ProductInventoryService {
    private final IProductInventoryRepository productInventoryRepository;
    private final ProductInventoryMapper productInventoryMapper;
    private final IProductRepository productRepository;
    private final IInventoryRepository inventoryRepository;
    private final InventoryService inventoryService;


    @Override
    @Transactional
    public ProductInventoryResponseDto createProductInventory(ProductInventoryRequestDto request) {
        log.info("Registrando receta: productId={}, inventoryId={}", 
            request.getProductId(), request.getInventoryId());
        
        // Obtener producto
        ProductModel product = productRepository.findById(request.getProductId())
            .orElseThrow(() -> {
                log.error("Producto no encontrado: {}", request.getProductId());
                return new NotFoundException("Producto no encontrado");
            });
        
        // Obtener insumo/ingrediente
        InventoryModel inventory = inventoryRepository.findById(request.getInventoryId())
            .orElseThrow(() -> {
                log.error("Inventario no encontrado: {}", request.getInventoryId());
                return new NotFoundException("Inventario no encontrado");
            });
        
        // Mapear y guardar
        ProductInventoryModel model = productInventoryMapper.toModel(request, product, inventory);
        ProductInventoryModel saved = productInventoryRepository.save(model);
        
        log.info("Relación registrada exitosamente con ID: {}", saved.getId());
        return productInventoryMapper.toDto(saved);
    }
  
    @Override
    public ProductInventoryResponseDto getProductInventoryById(Long id) {
        log.info("Buscando relación producto-inventario: {}", id);
        ProductInventoryModel model = productInventoryRepository.findById(id)
            .orElseThrow(() -> {
                log.error("Relación no encontrada: {}", id);
                return new NotFoundException("Relación producto-inventario no encontrada");
            });
        return productInventoryMapper.toDto(model);
    }

    //Checar si lo vuelvo paginable la lista de insumos para un producto
    @Override
    public List<ProductInventoryResponseDto> getRecipeByProductId(Long productId) {
        log.info("Obteniendo receta para producto ID: {}", productId);
        
        // Verificar que el producto existe
        if (!productRepository.existsById(productId)) {
            log.error("Producto no encontrado: {}", productId);
            throw new NotFoundException("Producto no encontrado");
        }
        
        List<ProductInventoryModel> models = productInventoryRepository.findByProductId(productId);

        if (models.isEmpty()) {
            log.warn("El producto {} no tiene receta definida", productId);
        }

        log.debug("Receta encontrada con {} ingredientes", models.size());
        
        return productInventoryMapper.toDto(models);
    }

    @Override
    public List<ProductInventoryResponseDto> getAllProductInventories() {
        log.info("Obteniendo todas las relaciones producto-inventario");
        List<ProductInventoryModel> models = productInventoryRepository.findAll();
        return productInventoryMapper.toDto(models);
    }
    
    
    @Override
    public boolean hasRecipe(Long productId) {
        log.debug("Verificando si producto {} tiene receta", productId);
        return productInventoryRepository.hasRecipe(productId);
    }
    
    // =============================================
    // HU3: MODIFICAR RECETAS
    // =============================================
    @Override
    @Transactional      
    public ProductInventoryResponseDto updateProductInventory(Long id, ProductInventoryRequestDto request) {
        log.info("Actualizando relación producto-inventario ID: {}", id);
        
        ProductInventoryModel existing = productInventoryRepository.findById(id)
            .orElseThrow(() -> {
                log.error("Relación no encontrada: {}", id);
                return new NotFoundException("Relación no encontrada");
            });

        // Verificar si el producto es una bebida o un producto con receta(falta añadir descartable)
        /*boolean isBeverage = existing.getProduct().getProductType().getName().equalsIgnoreCase("BEVERAGE");

        if (isBeverage) {   
            // Si es una bebida, solo actualizamos el inventario (quantity) y no la receta
            existing.getInventory().setQuantity(request.getQuantity());
        } else {*/
            // Si es un producto con receta (plato), actualizamos la cantidad y unidad de medida en la receta
            existing.setQuantity(request.getQuantity());
            existing.setUnitOfMeasure(request.getUnitOfMeasure());
        //}
        
        // Guardar la relación actualizada
        ProductInventoryModel updated = productInventoryRepository.save(existing);
        log.info("Relación actualizada: {}", id);
        
        return productInventoryMapper.toDto(updated);
    }
    
    // =============================================
    // HU5: VALIDACIÓN PARA VENTAS
    // =============================================
    @Override
    public boolean canSellProduct(Long productId, BigDecimal quantityToSell) {
        log.info("Verificando si se puede vender {} unidades del producto {}", quantityToSell, productId);
        
        // Obtener receta del producto
        List<ProductInventoryModel> recipe = productInventoryRepository.findByProductId(productId);
        
        if (recipe.isEmpty()) {
            log.warn("Producto {} no tiene receta registrada", productId);
            return false;
        }
        
        // Verificar que cada ingrediente tenga suficiente stock
        for (ProductInventoryModel ingredient : recipe) {
            BigDecimal requiredQuantity = ingredient.getQuantity().multiply(quantityToSell);
            
            if (!inventoryService.hasEnoughStock(ingredient.getInventory().getId(), requiredQuantity)) {
                log.warn("Stock insuficiente de {} para vender {} unidades del producto {}", 
                    ingredient.getInventory().getName(), quantityToSell, productId);
                return false;
            }
        }
        
        log.debug("Stock disponible para vender producto {}", productId);
        return true;
    }
    
    @Override                               
    public List<ProductInventoryResponseDto> getIngredientsForDeduction(Long productId) {
        log.info("Obteniendo ingredientes para deducir del producto {}", productId);
        List<ProductInventoryModel> models = productInventoryRepository.findIngredientsForDeduction(productId);
        return productInventoryMapper.toDto(models);                    
    }

    
    // =============================================
    // ELIMINAR
    // =============================================
    @Override
    @Transactional
    public void deleteProductInventory(Long id) {
        log.info("Eliminando relación producto-inventario ID: {}", id);
        
        if (!productInventoryRepository.existsById(id)) {
            log.error("Relación no encontrada: {}", id);
            throw new NotFoundException("Relación no encontrada");
        }
        
        productInventoryRepository.deleteById(id);
        log.info("Relación eliminada: {}", id);
    }

}