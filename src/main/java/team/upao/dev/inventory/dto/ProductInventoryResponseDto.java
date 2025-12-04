package team.upao.dev.inventory.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import team.upao.dev.inventory.enums.InventoryType;
import team.upao.dev.inventory.enums.UnitOfMeasure;

import java.math.BigDecimal;

@Getter @Setter
@Builder
public class ProductInventoryResponseDto {
    private Long id;
    private BigDecimal quantity;
    private UnitOfMeasure unitOfMeasure;
    
    // Datos del producto
    private Long productId;
    private String productName;
    
    // Datos del inventario
    private Long inventoryId;
    private String inventoryName;
    private InventoryType inventoryType;
    private BigDecimal inventoryQuantityAvailable;
}
