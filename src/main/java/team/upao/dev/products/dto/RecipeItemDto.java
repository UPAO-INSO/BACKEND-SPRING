package team.upao.dev.products.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import team.upao.dev.inventory.enums.UnitOfMeasure;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RecipeItemDto {
    @NotNull(message = "Inventory ID is required")
    private Long inventoryId;
    
    @NotNull(message = "Quantity is required")
    @Positive(message = "Quantity must be positive")
    private BigDecimal quantity;
    
    @NotNull(message = "Unit of measure is required")
    private UnitOfMeasure unitOfMeasure;
}
