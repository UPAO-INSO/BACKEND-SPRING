package team.upao.dev.integrations.nubefact.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;


@Builder
@Data
@AllArgsConstructor @NoArgsConstructor
public class NubefactReceiptRequestDto {
    @Builder.Default
    @NotNull
    @JsonSetter(nulls = Nulls.SKIP)
    @Pattern(regexp = "^generar_comprobante$", message = "operacion must be 'generar_comprobante'")
    private String operacion = "generar_comprobante";
    @JsonProperty("tipo_de_comprobante")
    @NotNull
    @JsonSetter(nulls = Nulls.SKIP)
    private Integer tipoDeComprobante = 2;
    @NotBlank
    private String serie;
    @NotNull
    private Integer numero;
    @JsonProperty("sunat_transaction")
    @NotNull
    private Integer sunatTransaction;
    @JsonProperty("cliente_tipo_de_documento")
    @NotNull
    @JsonSetter(nulls = Nulls.SKIP)
    private Integer clienteTipoDeDocumento = 1;
    @JsonProperty("cliente_numero_de_documento")
    @NotBlank
    @Size(min = 8, max = 8)
    private String clienteNumeroDeDocumento;
    @JsonProperty("cliente_denominacion")
    @NotBlank
    private String clienteDenominacion;
    @JsonProperty("cliente_direccion")
    @NotBlank
    private String clienteDireccion;
    @JsonProperty("cliente_email")
    @NotBlank
    @Email
    private String clienteEmail;
    @JsonProperty("cliente_email_1")
    @Email
    private String clienteEmail1; // Optional
    @JsonProperty("cliente_email_2")
    @Email
    private String clienteEmail2; // Optional
    @JsonProperty("fecha_de_emision")
    @NotBlank
    private String fechaDeEmision;
    @JsonProperty("fecha_de_vencimiento")
    private String fechaDeVencimiento; // Optional
    @NotNull
    @JsonSetter(nulls = Nulls.SKIP)
    private Integer moneda = 1;
    @JsonProperty("tipo_de_cambio")
    private String tipoDeCambio; // Optional
    @JsonProperty("porcentaje_de_igv")
    @NotNull
    @Builder.Default
    @JsonSetter(nulls = Nulls.SKIP)
    private Double porcentajeDeIgv = 18.00;
    @JsonProperty("descuento_global")
    private String descuentoGlobal; // Optional
    @JsonProperty("total_descuento")
    private String totalDescuento; // Optional
    @JsonProperty("total_anticipo")
    private String totalAnticipo; // Optional
    @JsonProperty("total_gravada")
    private Double totalGravada; // Optional
    @JsonProperty("total_inafecta")
    private String totalInafecta; // Optional
    @JsonProperty("total_exonerada")
    private String totalExonerada; // Optional
    @JsonProperty("total_igv")
    private Double totalIgv; // Optional
    @JsonProperty("total_gratuita")
    private String totalGratuita; // Optional
    @JsonProperty("total_otros_cargos")
    private String totalOtrosCargos; // Optional

    @NotNull
    private Double total;

    private String percepcionTipo; // Optional
    private String percepcionBaseImponible; // Optional
    private String totalPercepcion; // Optional
    private String totalIncluidoPercepcion; // Optional
    private String retencionTipo; // Optional
    private String retencionBaseImponible; // Optional
    private String totalRetencion; // Optional
    private String totalImpuestosBolsas; // Optional

    @Builder.Default
    @JsonSetter(nulls = Nulls.SKIP)
    private Boolean detraccion = false; // Optional
    @Builder.Default
    @JsonSetter(nulls = Nulls.SKIP)
    private String observaciones = "";

    private String documentoQueSeModificaTipo; // Optional
    private String documentoQueSeModificaSerie; // Optional
    private String documentoQueSeModificaNumero;
    private String tipoDeNotaDeCredito; // Optional
    private String tipoDeNotaDeDebito; // Optional

    @Builder.Default
    @JsonSetter(nulls = Nulls.SKIP)
    private Boolean enviarAutomaticamenteALaSunat = false; // Optional

    @Builder.Default
    @JsonSetter(nulls = Nulls.SKIP)
    private Boolean enviarAutomaticamenteAlCliente = true; // Optional

    private String condicionesDePago; // Optional
    private String medioDePago; // Optional
    private String placaVehiculo; // Optional
    private String ordenCompraServicio; // Optional

    @NotBlank
    private String formatoDePdf;

    private String generadoPorContingencia; // Optional
    private String bienesRegionSelva; // Optional
    private String serviciosRegionSelva; // Optional

    @Builder.Default
    @NotBlank
    @JsonSetter(nulls = Nulls.SKIP)
    private List<NubefactItemDto> items = new ArrayList<>();

    private List<NubefactGuideDto> guias; // Optional
    private List<NubefactInstallmentDto> ventaAlCredito; // Optional

    public void applyDefaults() {
        if (this.operacion == null || this.operacion.isBlank()) this.operacion = "generar_comprobante";
        if (this.tipoDeComprobante == null) this.tipoDeComprobante = 2;
        if (this.clienteTipoDeDocumento == null) this.clienteTipoDeDocumento = 1;
        if (this.moneda == null) this.moneda = 1;
        if (this.porcentajeDeIgv == null) this.porcentajeDeIgv = 18.00;
        if (this.detraccion == null) this.detraccion = false;
        if (this.observaciones == null) this.observaciones = "";
        if (this.enviarAutomaticamenteALaSunat == null) this.enviarAutomaticamenteALaSunat = false;
        if (this.enviarAutomaticamenteAlCliente == null) this.enviarAutomaticamenteAlCliente = true;
        if (this.items == null) this.items = new ArrayList<>();
        if (this.formatoDePdf == null || this.formatoDePdf.isBlank()) this.formatoDePdf = "A4";
    }
}
