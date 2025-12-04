package team.upao.dev.integrations.nubefact.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NubefactItemDto {
    @JsonProperty("unidad_de_medida")
    private String unidadDeMedida;
    private String codigo; // Optional
    @JsonProperty("codigo_producto_sunat")
    private String codigoProductoSunat; // Optional
    private String descripcion;
    private Double cantidad;
    @JsonProperty("valor_unitario")
    private Double valorUnitario;
    @JsonProperty("precio_unitario")
    private Double precioUnitario;
    private String descuento; // Optional
    private Double subtotal;
    @JsonProperty("tipo_de_igv")
    private Integer tipoDeIgv;
    private Double igv;
    private Double total;
    @JsonProperty("anticipo_regularizacion")
    private final Boolean anticipoRegularizacion = false; // Optional

    @JsonProperty("anticipo_documento_serie")
    private String anticipoDocumentoSerie; // Optional
    @JsonProperty("anticipo_documento_numero")
    private String anticipoDocumentoNumero; // Optional
}

