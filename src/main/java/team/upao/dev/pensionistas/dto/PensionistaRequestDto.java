package team.upao.dev.pensionistas.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class PensionistaRequestDto {

    @NotNull(message = "El cliente es obligatorio")
    private Long customerId;

    @NotNull(message = "La cantidad de créditos es obligatoria")
    @Positive(message = "Los créditos deben ser mayor a 0")
    private Integer planCredits;

    @NotNull(message = "El precio por almuerzo es obligatorio")
    @Positive(message = "El precio debe ser mayor a 0")
    private Double planPricePerMeal;

    private String notes;
}
