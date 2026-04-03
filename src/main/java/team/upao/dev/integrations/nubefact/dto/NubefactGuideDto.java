package team.upao.dev.integrations.nubefact.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NubefactGuideDto {
    @JsonProperty("guia_tipo")
    private Integer guiaTipo;
    @JsonProperty("guia_serie_numero")
    private String guiaSerieNumero;
}

