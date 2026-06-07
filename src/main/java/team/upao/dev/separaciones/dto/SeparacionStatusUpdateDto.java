package team.upao.dev.separaciones.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import team.upao.dev.separaciones.enums.SeparacionStatus;

@Getter @Setter
public class SeparacionStatusUpdateDto {

    @NotNull(message = "El estado es obligatorio")
    private SeparacionStatus status;
}
