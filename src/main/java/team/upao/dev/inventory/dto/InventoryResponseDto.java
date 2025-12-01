package team.upao.dev.inventory.dto;

import team.upao.dev.inventory.enums.InventoryType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class InventoryResponseDto {

    private Long id;  // ID del ítem en inventario
    private String name;  // Nombre del ítem
    private BigDecimal quantity;  // Cantidad disponible
    private InventoryType type;  // Tipo de objeto de inventario: 'BEVERAGE', 'INGREDIENT', 'DISPOSABLE'
    private String unitOfMeasure;  // Unidad de medida (kg, litro, unidad)
}
