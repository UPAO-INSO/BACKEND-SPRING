package team.upao.dev.products.service.impl;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import team.upao.dev.common.dto.PaginationRequestDto;
import team.upao.dev.common.dto.PaginationResponseDto;
import team.upao.dev.common.utils.PaginationUtils;
import team.upao.dev.exceptions.DuplicateResourceException;
import team.upao.dev.exceptions.ResourceNotFoundException;
import team.upao.dev.inventory.dto.ProductInventoryRequestDto;
import team.upao.dev.inventory.dto.ProductInventoryResponseDto;
import team.upao.dev.inventory.dto.ProductInventoryUpdateDto;
import team.upao.dev.inventory.dto.InventoryRequestDto;
import team.upao.dev.inventory.dto.InventoryResponseDto;
import team.upao.dev.inventory.enums.InventoryType;
import team.upao.dev.inventory.enums.UnitOfMeasure;
import team.upao.dev.inventory.service.ProductInventoryService;
import team.upao.dev.inventory.service.InventoryService;
import team.upao.dev.products.dto.ProductRequestDto;
import team.upao.dev.products.dto.ProductResponseDto;
import team.upao.dev.products.dto.RecipeItemDto;
import team.upao.dev.products.mapper.ProductMapper;
import team.upao.dev.products.model.ProductModel;
import team.upao.dev.products.model.ProductTypeModel;
import team.upao.dev.products.repository.IProductRepository;
import team.upao.dev.products.service.ProductService;
import team.upao.dev.products.service.ProductTypeService;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final IProductRepository productRepository;
    private final ProductTypeService productTypeService;
    private final ProductMapper productMapper;
    private final ProductInventoryService productInventoryService;
    private final InventoryService inventoryService;

    @Override
    public List<ProductResponseDto> findByIds(List<Long> ids) {
        List<ProductModel> products = productRepository.findAllById(ids);

        if (products.isEmpty()) {
            throw new ResourceNotFoundException("No products found with the provided IDs.");
        }

        return productMapper.toDto(products)
                .stream()
                .filter(p -> p.getActive() == true)
                .toList();
    }

    @Override
    public PaginationResponseDto<ProductResponseDto> findAll(PaginationRequestDto requestDto) {
        final Pageable pageable = PaginationUtils.getPageable(requestDto);
        final Page<ProductModel> entities = productRepository.findAll(pageable);
        final List<ProductResponseDto> productResponseDtos = productMapper.toDto(entities.getContent())
                .stream()
                .filter(p -> p.getActive() == true)
                .toList();
        return new PaginationResponseDto<>(
                productResponseDtos,
                entities.getTotalPages(),
                entities.getTotalElements(),
                entities.getSize(),
                entities.getNumber() + 1,
                entities.isEmpty()
        );
    }

    public PaginationResponseDto<ProductResponseDto> findAllByProductTypeId(Long productTypeId, PaginationRequestDto requestDto) {
        final Pageable pageable = PaginationUtils.getPageable(requestDto);
        final Page<ProductModel> entities = productRepository.findAllByProductTypeId(productTypeId, pageable);
        final List<ProductResponseDto> productResponseDtos = productMapper.toDto(entities.getContent())
                .stream()
                .filter(p -> p.getActive() == true)
                .toList();
        return new PaginationResponseDto<>(
                productResponseDtos,
                entities.getTotalPages(),
                entities.getTotalElements(),
                entities.getSize(),
                entities.getNumber() + 1,
                entities.isEmpty()
        );
    }

    @Override
    public ProductResponseDto findByName(String name) {
        ProductModel product = productRepository
                .findByNameIgnoreCase(name).stream().filter(p -> p.getActive() == true)
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with name: " + name));

        return productMapper.toDto(product);
    }

    @Override
    public List<ProductResponseDto> findByNameContaining(String name) {
        List<ProductModel> products = productRepository
                .findByNameContainingIgnoreCase(name)
                .orElseThrow(() -> new ResourceNotFoundException("No products found containing name: " + name));

        return productMapper.toDto(products)
                .stream()
                .filter(p -> p.getActive() == true)
                .toList();
    }

    @Override
    public ProductResponseDto findById(Long id) {
        ProductModel product = productRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));

        return productMapper.toDto(product);
    }

    @Override
    public ProductModel findModelById(Long id) {
        return productRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));
    }

    @Override
    public boolean existsByName(String name) {
        return productRepository.existsByName(name);
    }

    @Override
    @Transactional
    public ProductResponseDto create(ProductRequestDto product) {
        if (productRepository.existsByName(product.getName()))
            throw new DuplicateResourceException("Product with name: " + product.getName() + " already exists");

        ProductModel productModel = productMapper.toModel(product);
        ProductTypeModel productType = productTypeService.findModelById(product.getProductTypeId());

        productModel.setProductType(productType);

        productRepository.save(productModel);

        String productTypeName = productType.getName().toUpperCase();
        
        // Si es BEBIDAS o DESCARTABLES, crear registro en inventario y relación automática
        if (productTypeName.equals("BEBIDAS") || productTypeName.equals("DESCARTABLES")) {
            log.info("Creating inventory entry for {} product: {}", productTypeName, product.getName());
            
            // Determinar el tipo de inventario según el tipo de producto
            InventoryType inventoryType = productTypeName.equals("BEBIDAS") 
                    ? InventoryType.BEVERAGE 
                    : InventoryType.DISPOSABLE;
            
            // Crear el item en inventario con la cantidad inicial
            InventoryRequestDto inventoryRequest = InventoryRequestDto.builder()
                    .name(product.getName())
                    .quantity(product.getInitialQuantity() != null ? product.getInitialQuantity() : java.math.BigDecimal.ONE)
                    .type(inventoryType)
                    .unitOfMeasure(UnitOfMeasure.UNIDAD) // Bebidas y descartables siempre en UNIDAD
                    .build();
            
            InventoryResponseDto inventoryCreated = inventoryService.create(inventoryRequest);
            log.info("Inventory item created with ID: {}", inventoryCreated.getId());
            
            // Crear la relación producto-inventario con cantidad 1 (se descuenta 1 por cada venta)
            ProductInventoryRequestDto relationRequest = ProductInventoryRequestDto.builder()
                    .productId(productModel.getId())
                    .inventoryId(inventoryCreated.getId())
                    .quantity(java.math.BigDecimal.ONE)
                    .unitOfMeasure(UnitOfMeasure.UNIDAD)
                    .build();
            
            productInventoryService.createProductInventory(relationRequest);
            log.info("Product-Inventory relation created for product: {}", productModel.getId());
            
        } else if (product.getRecipe() != null && !product.getRecipe().isEmpty()) {
            // Para platos (ENTRADAS, SEGUNDOS, CARTA), crear receta con ingredientes
            for (RecipeItemDto item : product.getRecipe()) {
                ProductInventoryRequestDto inventoryRequest = ProductInventoryRequestDto.builder()
                        .productId(productModel.getId())
                        .inventoryId(item.getInventoryId())
                        .quantity(item.getQuantity())
                        .unitOfMeasure(item.getUnitOfMeasure())
                        .build();
                productInventoryService.createProductInventory(inventoryRequest);
            }
        }

        return productMapper.toDto(productModel);
    }

    @Override
    @Transactional
    public ProductResponseDto update(Long id, ProductRequestDto product) {
        ProductModel productExisting = findModelById(id);
        
        // Detectar cambio de categoría
        Long oldTypeId = productExisting.getProductType().getId();
        Long newTypeId = product.getProductTypeId();
        boolean categoryChanged = newTypeId != null && !newTypeId.equals(oldTypeId);
        
        // Obtener nombres de categorías para determinar el tipo de cambio
        String oldTypeName = productExisting.getProductType().getName().toUpperCase();
        boolean wasPlato = oldTypeName.equals("ENTRADAS") || oldTypeName.equals("SEGUNDOS") || oldTypeName.equals("CARTA");
        boolean wasBeverageOrDisposable = oldTypeName.equals("BEBIDAS") || oldTypeName.equals("DESCARTABLES");
        
        String newTypeName = "";
        boolean willBePlato = false;
        boolean willBeBeverageOrDisposable = false;
        
        if (newTypeId != null) {
            ProductTypeModel newType = productTypeService.findModelById(newTypeId);
            newTypeName = newType.getName().toUpperCase();
            willBePlato = newTypeName.equals("ENTRADAS") || newTypeName.equals("SEGUNDOS") || newTypeName.equals("CARTA");
            willBeBeverageOrDisposable = newTypeName.equals("BEBIDAS") || newTypeName.equals("DESCARTABLES");
        }

        productExisting.setName(product.getName());
        productExisting.setDescription(product.getDescription());
        productExisting.setPrice(product.getPrice());
        productExisting.setAvailable(product.getAvailable() != null ? product.getAvailable() : true);
        productExisting.setActive(product.getActive() != null ? product.getActive() : true);
        
        // Actualizar tipo de producto si cambió
        if (categoryChanged) {
            ProductTypeModel newProductType = productTypeService.findModelById(newTypeId);
            productExisting.setProductType(newProductType);
            log.info("Category changed from {} to {} for product {}", oldTypeName, newTypeName, id);
        }

        productRepository.save(productExisting);
        
        // Manejar cambio de categoría
        if (categoryChanged) {
            List<ProductInventoryResponseDto> existingRecipeList = productInventoryService.getRecipeByProductId(id);
            
            if (wasBeverageOrDisposable && willBePlato) {
                // De bebida/descartable a plato: eliminar inventario asociado
                log.info("Changing from beverage/disposable to dish - removing inventory association");
                for (ProductInventoryResponseDto existing : existingRecipeList) {
                    productInventoryService.deleteProductInventory(existing.getId());
                    // También eliminar el inventory si tiene el mismo nombre del producto
                    if (existing.getInventoryName() != null && 
                        existing.getInventoryName().equalsIgnoreCase(productExisting.getName())) {
                        try {
                            inventoryService.delete(existing.getInventoryId());
                            log.info("Deleted inventory {} for product {}", existing.getInventoryId(), id);
                        } catch (Exception e) {
                            log.warn("Could not delete inventory {}: {}", existing.getInventoryId(), e.getMessage());
                        }
                    }
                }
            } else if (wasPlato && willBeBeverageOrDisposable) {
                // De plato a bebida/descartable: eliminar receta y crear inventario
                log.info("Changing from dish to beverage/disposable - removing recipe and creating inventory");
                for (ProductInventoryResponseDto existing : existingRecipeList) {
                    productInventoryService.deleteProductInventory(existing.getId());
                }
                
                // Crear nuevo inventory para el producto
                InventoryType invType = newTypeName.equals("BEBIDAS") ? InventoryType.BEVERAGE : InventoryType.DISPOSABLE;
                InventoryRequestDto inventoryRequest = InventoryRequestDto.builder()
                        .name(productExisting.getName())
                        .quantity(product.getInitialQuantity() != null ? product.getInitialQuantity() : java.math.BigDecimal.ZERO)
                        .type(invType)
                        .unitOfMeasure(UnitOfMeasure.UNIDAD)
                        .build();
                InventoryResponseDto createdInventory = inventoryService.create(inventoryRequest);
                
                // Crear relación product_inventory
                ProductInventoryRequestDto piRequest = ProductInventoryRequestDto.builder()
                        .productId(id)
                        .inventoryId(createdInventory.getId())
                        .quantity(java.math.BigDecimal.ONE)
                        .unitOfMeasure(UnitOfMeasure.UNIDAD)
                        .build();
                productInventoryService.createProductInventory(piRequest);
                log.info("Created inventory {} and product_inventory for product {}", createdInventory.getId(), id);
                
                return productMapper.toDto(productExisting);
            } else if (wasBeverageOrDisposable && willBeBeverageOrDisposable) {
                // De bebida a descartable o viceversa: actualizar tipo en inventory
                log.info("Changing between beverage/disposable - updating inventory type");
                for (ProductInventoryResponseDto existing : existingRecipeList) {
                    InventoryType newInvType = newTypeName.equals("BEBIDAS") ? InventoryType.BEVERAGE : InventoryType.DISPOSABLE;
                    inventoryService.update(existing.getInventoryId(), 
                        team.upao.dev.inventory.dto.InventoryUpdateDto.builder()
                            .type(newInvType)
                            .build());
                }
            }
        }

        // Update Recipe (solo para platos)
        if (product.getRecipe() != null && !willBeBeverageOrDisposable) {
            log.info("Updating recipe for product {}. New recipe size: {}", id, product.getRecipe().size());
            
            List<ProductInventoryResponseDto> existingRecipeList = productInventoryService.getRecipeByProductId(id);
            log.info("Existing recipe size: {}", existingRecipeList.size());
            
            Map<Long, ProductInventoryResponseDto> existingRecipeMap = existingRecipeList.stream()
                    .collect(Collectors.toMap(ProductInventoryResponseDto::getInventoryId, Function.identity(), (a, b) -> a));
            
            List<RecipeItemDto> newRecipe = product.getRecipe();
            Set<Long> newInventoryIds = newRecipe.stream()
                    .map(RecipeItemDto::getInventoryId)
                    .collect(Collectors.toSet());

            // 1. Delete items not in new recipe
            for (ProductInventoryResponseDto existing : existingRecipeList) {
                if (!newInventoryIds.contains(existing.getInventoryId())) {
                    log.info("Deleting recipe item: {}", existing.getId());
                    productInventoryService.deleteProductInventory(existing.getId());
                }
            }

            // 2. Create or Update items
            for (RecipeItemDto newItem : newRecipe) {
                ProductInventoryResponseDto existing = existingRecipeMap.get(newItem.getInventoryId());

                if (existing == null) {
                    log.info("Creating new recipe item for inventory: {}", newItem.getInventoryId());
                    // Create
                    ProductInventoryRequestDto createRequest = ProductInventoryRequestDto.builder()
                            .productId(id)
                            .inventoryId(newItem.getInventoryId())
                            .quantity(newItem.getQuantity())
                            .unitOfMeasure(newItem.getUnitOfMeasure())
                            .build();
                    productInventoryService.createProductInventory(createRequest);
                } else {
                    // Update if changed
                    // Use compareTo for BigDecimal to ignore scale differences (e.g. 1.00 vs 1)
                    boolean quantityChanged = existing.getQuantity().compareTo(newItem.getQuantity()) != 0;
                    boolean unitChanged = !existing.getUnitOfMeasure().equals(newItem.getUnitOfMeasure());
                    
                    if (quantityChanged || unitChanged) {
                        log.info("Updating recipe item: {}. Qty changed: {}, Unit changed: {}", 
                                existing.getId(), quantityChanged, unitChanged);
                        
                        ProductInventoryUpdateDto updateRequest = ProductInventoryUpdateDto.builder()
                                .quantity(newItem.getQuantity())
                                .unitOfMeasure(newItem.getUnitOfMeasure())
                                .build();
                        productInventoryService.updateProductInventory(existing.getId(), updateRequest);
                    }
                }
            }
        } else {
            log.info("No recipe provided for update (null)");
        }        return productMapper.toDto(productExisting);
    }

    @Override
    @Transactional
    public ProductResponseDto partialUpdate(Long id, ProductResponseDto product) {
        findModelById(id);

        if (product.getName() != null && !product.getName().isBlank()) {
            updateNameById(id, product.getName());
        }

        if (product.getPrice() != null && product.getPrice() >= 0) {
            updatePriceById(id, product.getPrice());
        }

        if (product.getDescription() != null && !product.getDescription().isBlank()) {
            updateDescriptionById(id, product.getDescription());
        }

        if (product.getProductTypeId() != null) {
            Long productTypeId = productTypeService.findModelById(product.getProductTypeId()).getId();
            updateProductTypeById(id, productTypeId);
        }

        if (product.getActive() != null) {
            updateActiveById(id, product.getActive());
        }

        if (product.getAvailable() != null) {
            updateAvailableById(id, product.getAvailable());
        }

        ProductModel productExisting = findModelById(id);

        return productMapper.toDto(productExisting);
    }

    @Override
    @Transactional
    public void updateNameById(Long id, String name) {
        this.findById(id);

        if (existsByName(name)) {
            throw new DuplicateResourceException("Product with name: " + name + " already exists");
        }

        this.productRepository.updateNameById(id, name);
    }

    @Override
    @Transactional
    public void updatePriceById(Long id, Double price) {
        this.findById(id);

        if (price < 0) {
            throw new IllegalArgumentException("Price cannot be negative");
        }

        this.productRepository.updatePriceById(id, price);
    }

    @Override
    @Transactional
    public void updateDescriptionById(Long id, String description) {
        this.findById(id);

        if (description == null || description.isBlank()) {
            throw new IllegalArgumentException("Description cannot be null or empty");
        }

        this.productRepository.updateDescriptionById(id, description);
    }

    @Override
    @Transactional
    public void updateProductTypeById(Long id, Long productTypeId) {
        this.findById(id);
        ProductTypeModel productType = productTypeService.findModelById(productTypeId);

        this.productRepository.updateProductTypeById(id, productType);
    }

    @Override
    @Transactional
    public void updateAvailableById(Long id, Boolean available) {
        this.findById(id);

        if (available == null) {
            throw new IllegalArgumentException("Available status cannot be null");
        }

        this.productRepository.updateAvailableById(id, available);
    }

    @Override
    @Transactional
    public void updateActiveById(Long id, Boolean active) {
        this.findById(id);

        if (active == null) {
            throw new IllegalArgumentException("Active status cannot be null");
        }

        this.productRepository.updateActiveById(id, active);
    }

    @Override
    @Transactional
    public String delete(Long id) {
        ProductModel product = this.findModelById(id);

        product.setActive(false);

        productRepository.save(product);

        return "Product with id " + id + " has been marked as unavailable.";
    }
}
