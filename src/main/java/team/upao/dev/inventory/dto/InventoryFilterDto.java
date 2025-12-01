package team.upao.dev.inventory.dto;

import team.upao.dev.inventory.enums.InventoryType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.Size;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InventoryFilterDto {

    private InventoryType type;

    private List<InventoryType> types;  // Tipo de ítem: 'BEVERAGE', 'INGREDIENT', 'DISPOSABLE'

    @Size(min = 1, max = 255, message = "El nombre debe tener entre 1 y 255 caracteres")
    private String searchTerm;  // Nombre del ítem (opcional para filtrado por nombre)
}
