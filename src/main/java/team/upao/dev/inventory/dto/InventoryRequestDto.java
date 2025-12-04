package team.upao.dev.inventory.dto;

import team.upao.dev.inventory.enums.InventoryType;
import team.upao.dev.inventory.enums.UnitOfMeasure;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class InventoryRequestDto {

    @NotBlank(message = "El nombre del ítem no puede ser nulo")
    private String name;  // Nombre del insumo o producto

    @NotNull(message = "La cantidad es requerida")
    @DecimalMin(value = "0.01", message = "Debe ser mayor a 0")
    private BigDecimal quantity;  // Cantidad disponible en inventario

    @NotNull(message = "El tipo es requerido")
    private InventoryType type;  // Tipo: 'BEVERAGE', 'INGREDIENT', 'DISPOSABLE'

    @NotNull(message = "La unidad de medida es requerida")
    private UnitOfMeasure unitOfMeasure;
}