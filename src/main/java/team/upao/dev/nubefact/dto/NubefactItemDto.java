package team.upao.dev.nubefact.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class NubefactItemDto {
    private String unidadDeMedida;
    private String codigo; // Optional
    private String codigoProductoSunat; // Optional
    private String descripcion;
    private Double cantidad;
    private Double valorUnitario;
    private Double precioUnitario;
    private String descuento; // Optional
    private Double subtotal;
    private Integer tipoDeIgv;
    private Double igv;
    private Double total;
    private final Boolean anticipoRegularizacion = false; // Optional
    private String anticipoDocumentoSerie; // Optional
    private String anticipoDocumentoNumero; // Optional
}

