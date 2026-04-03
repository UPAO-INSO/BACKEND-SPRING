package team.upao.dev.inventory.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import team.upao.dev.inventory.dto.ProductInventoryRequestDto;
import team.upao.dev.inventory.dto.ProductInventoryResponseDto;
import team.upao.dev.inventory.model.ProductInventoryModel;
import team.upao.dev.products.model.ProductModel;
import team.upao.dev.inventory.model.InventoryModel;
import java.util.List;

@Component
@RequiredArgsConstructor
public class ProductInventoryMapper {
    public ProductInventoryModel toModel(ProductInventoryRequestDto dto, ProductModel product, InventoryModel inventory) {
        return ProductInventoryModel.builder()
                .quantity(dto.getQuantity())
                .unitOfMeasure(dto.getUnitOfMeasure())
                .product(product)           
                .inventory(inventory)       
                .build();
    }
    
    public ProductInventoryResponseDto toDto(ProductInventoryModel model) {
        return ProductInventoryResponseDto.builder()
                .id(model.getId())
                .quantity(model.getQuantity())
                .unitOfMeasure(model.getUnitOfMeasure())
                // ← Extrae info de ProductModel
                .productId(model.getProduct().getId())
                .productName(model.getProduct().getName())
                // ← Extrae info de InventoryModel
                .inventoryId(model.getInventory().getId())
                .inventoryName(model.getInventory().getName())
                .inventoryType(model.getInventory().getType())
                .inventoryQuantityAvailable(model.getInventory().getQuantity())
                .build();
    }
    
    /**
     * Convierte List<ProductInventoryModel> → List<ProductInventoryResponseDto>
     */
    public List<ProductInventoryResponseDto> toDto(List<ProductInventoryModel> models) {
        return models.stream()
                .map(this::toDto)
                .toList();
    }
}