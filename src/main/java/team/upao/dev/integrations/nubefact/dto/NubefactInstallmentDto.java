package team.upao.dev.integrations.nubefact.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NubefactInstallmentDto {
    private Integer cuota;
    @JsonProperty("fecha_de_pago")
    private String fechaDePago;
    private Double importe;
}

