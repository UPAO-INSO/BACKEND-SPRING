package team.upao.dev.menu.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class MenuDiarioItemRequestDto {

    @NotNull(message = "El producto es obligatorio")
    private Long productId;

    @NotNull(message = "Las porciones estimadas son obligatorias")
    @Positive(message = "Las porciones deben ser mayor a 0")
    private Integer estimatedPortions;
}
