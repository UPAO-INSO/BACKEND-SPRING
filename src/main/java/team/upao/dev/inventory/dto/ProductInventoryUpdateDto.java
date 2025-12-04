package team.upao.dev.inventory.dto;

import team.upao.dev.inventory.enums.UnitOfMeasure;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductInventoryUpdateDto {

    private BigDecimal quantity;         // opcional
    private UnitOfMeasure unitOfMeasure; // opcional
    private Long productId;              // opcional
    private Long inventoryId;            // opcional
}