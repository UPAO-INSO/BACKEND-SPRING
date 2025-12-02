package team.upao.dev.inventory.dto;

import team.upao.dev.inventory.enums.UnitOfMeasure;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.DecimalMin;
import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductInventoryRequestDto {
   
    @NotNull(message = "La cantidad es requerida")
    @DecimalMin(value = "0.0", inclusive = false, message = "La cantidad debe ser mayor a 0")
    private BigDecimal quantity;  // Cantidad disponible en inventario

    @NotNull(message = "La unidad de medida es requerida")
    private UnitOfMeasure unitOfMeasure;

    @NotNull(message = "El producto asociado es obligatorio")
    private Long productId;  // ID del producto asociado (plato o bebida)

    @NotNull(message = "El inventario asociado es obligatorio")
    private Long inventoryId;  // ID del inventario asociado (insumo o bebida)
}