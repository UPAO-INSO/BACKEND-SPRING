package team.upao.dev.inventory.service.Impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import team.upao.dev.exceptions.ResourceNotFoundException;
import team.upao.dev.inventory.dto.ProductInventoryRequestDto;
import team.upao.dev.inventory.dto.ProductInventoryResponseDto;
import team.upao.dev.inventory.dto.ProductInventoryUpdateDto;
import team.upao.dev.inventory.enums.UnitOfMeasure;
import team.upao.dev.inventory.mapper.ProductInventoryMapper;
import team.upao.dev.inventory.model.InventoryModel;
import team.upao.dev.inventory.model.ProductInventoryModel;
import team.upao.dev.inventory.repository.IInventoryRepository;
import team.upao.dev.inventory.repository.IProductInventoryRepository;
import team.upao.dev.inventory.service.InventoryValidationService;
import team.upao.dev.inventory.service.InventoryConversionService;
import team.upao.dev.inventory.service.ProductInventoryService;
import team.upao.dev.products.model.ProductModel;
import team.upao.dev.products.repository.IProductRepository;

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
    private final InventoryValidationService validationService;
    private final InventoryConversionService conversionService;


    @Override
    @Transactional
    public ProductInventoryResponseDto createProductInventory(ProductInventoryRequestDto request) {
        log.info("Registrando receta: productId={}, inventoryId={}", 
            request.getProductId(), request.getInventoryId());

        validationService.validateQuantity(request.getQuantity());
        validationService.validateUnit(request.getUnitOfMeasure());

        // Obtener producto
        ProductModel product = productRepository.findById(request.getProductId())
            .orElseThrow(() -> {
                log.error("Producto no encontrado: {}", request.getProductId());
                return new ResourceNotFoundException("Producto no encontrado");
            });
        
        // Obtener insumo/ingrediente
        InventoryModel inventory = inventoryRepository.findById(request.getInventoryId())
            .orElseThrow(() -> {
                log.error("Inventario no encontrado: {}", request.getInventoryId());
                return new ResourceNotFoundException("Inventario no encontrado");
            });
        
        ProductInventoryModel tempModel = ProductInventoryModel.builder()
            .unitOfMeasure(request.getUnitOfMeasure())
            .inventory(inventory)
            .product(product)
            .build();
        
        validationService.validateUnitCompatibility(tempModel, inventory);    

        // Mapear y guardar
        ProductInventoryModel model = productInventoryMapper.toModel(request, product, inventory);
        ProductInventoryModel saved = productInventoryRepository.save(model);
        
        log.info("Relación registrada: ID={}, Producto={}, Ingrediente={}, Cantidad={} {}", 
            saved.getId(), product.getName(), inventory.getName(),
            saved.getQuantity(), saved.getUnitOfMeasure().getSymbol());
        return productInventoryMapper.toDto(saved);
    }
  
    @Override
    public ProductInventoryResponseDto getProductInventoryById(Long id) {
        log.info("Buscando relación producto-inventario: {}", id);
        ProductInventoryModel model = productInventoryRepository.findById(id)
            .orElseThrow(() -> {
                log.error("Relación no encontrada: {}", id);
                return new ResourceNotFoundException("Relación producto-inventario no encontrada");
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
            throw new ResourceNotFoundException("Producto no encontrado");
        }
        
        List<ProductInventoryModel> models = productInventoryRepository.findByProductId(productId);


        if (models.isEmpty()) {
            log.warn("El producto {} no tiene receta definida", productId);
        }

        log.info("Receta encontrada con {} ingredientes:", models.size());
        models.forEach(m -> 
            log.info(" → Ingrediente: {} ({})", m.getInventory().getName(), m.getQuantity())
        );
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
    public ProductInventoryResponseDto updateProductInventory(Long id, ProductInventoryUpdateDto request) {
        log.info("Actualizando relación producto-inventario ID: {}", id);

        validationService.validateQuantity(request.getQuantity());
        validationService.validateUnit(request.getUnitOfMeasure());
        
        ProductInventoryModel existing = productInventoryRepository.findById(id)
            .orElseThrow(() -> {
                log.error("Relación no encontrada: {}", id);
                return new ResourceNotFoundException("Relación no encontrada");
            });
       ProductModel currentProduct = existing.getProduct();
       InventoryModel currentInventory = existing.getInventory();

        // Guardar unidades anteriores por si hay cambio
        UnitOfMeasure oldUnit = existing.getUnitOfMeasure();

        
        ProductModel newProduct = currentProduct;
        if (request.getProductId() != null && 
            !request.getProductId().equals(currentProduct.getId())) {

            newProduct = productRepository.findById(request.getProductId())
                .orElseThrow(() -> {
                    log.error("Producto no encontrado: {}", request.getProductId());
                    return new ResourceNotFoundException("Producto no encontrado");
                });

            log.info("Producto actualizado en la relación: {} → {}", 
                currentProduct.getId(), newProduct.getId());
        }

        
        InventoryModel newInventory = currentInventory;
        if (request.getInventoryId() != null && 
            !request.getInventoryId().equals(currentInventory.getId())) {

            newInventory = inventoryRepository.findById(request.getInventoryId())
                .orElseThrow(() -> {
                    log.error("Inventario no encontrado: {}", request.getInventoryId());
                    return new ResourceNotFoundException("Inventario no encontrado");
                });

            log.info("Inventario actualizado en la relación: {} → {}", 
                currentInventory.getId(), newInventory.getId());
        }

        
        try {
            // Reglas de negocio entre tipo–unidad–cantidad
            validationService.validateTypeAndUnitConsistency(
                newInventory.getType(),
                request.getUnitOfMeasure(),
                request.getQuantity()
            );

            // Reglas de compatibilidad de unidades (receta vs inventario)
            ProductInventoryModel tempModel = ProductInventoryModel.builder()
                .unitOfMeasure(request.getUnitOfMeasure())
                .inventory(newInventory)
                .product(newProduct)
                .build();

            validationService.validateUnitCompatibility(tempModel, newInventory);

            // Validar cambios específicos de unidad (si se cambió)
            if (!oldUnit.equals(request.getUnitOfMeasure())) {
                validationService.validateUnitChange(
                    newInventory.getType(),
                    oldUnit,
                    request.getUnitOfMeasure(),
                    request.getQuantity()
                );
            }

        } catch (IllegalArgumentException e) {
            log.error("Validación de actualización fallida: {}", e.getMessage());
            throw e;
        }

         existing.setQuantity(request.getQuantity());
         existing.setUnitOfMeasure(request.getUnitOfMeasure());
         existing.setProduct(newProduct);
         existing.setInventory(newInventory);
      
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
        
        try {
            validationService.validateQuantity(quantityToSell);
            
            // Obtener receta
            List<ProductInventoryModel> recipe = productInventoryRepository.findByProductId(productId);
            
            if (recipe.isEmpty()) {
                log.warn("Producto {} sin receta", productId);
                return false;
            }
            
            log.debug("Receta tiene {} ingredientes", recipe.size());
            
            //verificar stock de cada ingrediente
            for (ProductInventoryModel ingredient : recipe) {
                InventoryModel inventory = ingredient.getInventory();
                
                log.debug("Verificando ingrediente: {} (unidad receta: {}, unidad inventario: {})", 
                    inventory.getName(), 
                    ingredient.getUnitOfMeasure().getSymbol(),
                    inventory.getUnitOfMeasure().getSymbol()
                );

                // Calcular cantidad total a deducir
                BigDecimal totalDeduction = conversionService.calculateTotalDeductionQuantity(
                    ingredient.getQuantity(),
                    quantityToSell,
                    ingredient.getUnitOfMeasure(),
                    inventory
                );
                
                log.debug("Ingrediente '{}': necesita {} {}", 
                    inventory.getName(), totalDeduction, inventory.getUnitOfMeasure().getSymbol());
                
                // Verificar si hay suficiente stock
                if (inventory.getQuantity().compareTo(totalDeduction) < 0) {
                    log.error(
                        "Stock insuficiente de '{}': Disponible {} {}, Necesita {} {}",
                        inventory.getName(),
                        inventory.getQuantity(), inventory.getUnitOfMeasure().getSymbol(),
                        totalDeduction, inventory.getUnitOfMeasure().getSymbol()
                    );
                    return false;
                }
            }
            
            log.info("Stock suficiente para vender {} unidades del producto {}", 
                quantityToSell, productId);
            return true;
            
        } catch (Exception e) {
            log.error("Error al verificar disponibilidad: {}", e.getMessage());
            return false;
        }
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
            throw new ResourceNotFoundException("Relación no encontrada");
        }
        
        productInventoryRepository.deleteById(id);
        log.info("Relación eliminada: {}", id);
    }

}