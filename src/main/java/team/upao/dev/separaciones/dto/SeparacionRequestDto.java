package team.upao.dev.separaciones.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter @Setter
public class SeparacionRequestDto {

    private Long pensionistaId;
    private Long customerId;
    private String clientName;
    private String notes;

    @NotEmpty(message = "La separación debe tener al menos un ítem")
    @Valid
    private List<SeparacionItemRequestDto> items;
}
