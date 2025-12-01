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
public class InventoryUpdateDto {

    private String name;  // Nombre del insumo o producto

    private BigDecimal quantity;  // Cantidad disponible en inventario

    private InventoryType type;  // Tipo: 'BEVERAGE', 'INGREDIENT', 'DISPOSABLE'

    private String unitOfMeasure;  // Unidad de medida (kg, litro, unidades de botella)
}
