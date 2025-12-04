package team.upao.dev.integrations.nubefact.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor @NoArgsConstructor
@Builder
public class NubefactInvoiceRequestDto {
    @Builder.Default
    private String operacion = "generar_comprobante";
    @JsonProperty("tipo_de_comprobante")
    @NotBlank
    private Integer tipoDeComprobante = 1;
    @NotBlank
    private String serie;
    @NotBlank
    private Integer numero;
    @JsonProperty("sunat_transaction")
    @NotBlank
    private Integer sunatTransaction;
    @JsonProperty("cliente_tipo_de_documento")
    @NotBlank
    private Integer clienteTipoDeDocumento;
    @JsonProperty("cliente_numero_de_documento")
    @NotBlank
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
    @NotBlank
    private Integer moneda;
    @JsonProperty("tipo_de_cambio")
    private String tipoDeCambio; // Optional
    @NotBlank
    @Builder.Default
    private Double porcentajeDeIgv = 18.00;

    private String descuentoGlobal; // Optional
    private String totalDescuento; // Optional
    private String totalAnticipo; // Optional
    private Double totalGravada; // Optional
    private String totalInafecta; // Optional
    private String totalExonerada; // Optional
    private Double totalIgv; // Optional
    private String totalGratuita; // Optional
    private String totalOtrosCargos; // Optional

    @NotBlank
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
    private Boolean detraccion = false; // Optional
    @Builder.Default
    private String observaciones = "";

    private String documentoQueSeModificaTipo; // Optional
    private String documentoQueSeModificaSerie; // Optional
    private String documentoQueSeModificaNumero;
    private String tipoDeNotaDeCredito; // Optional
    private String tipoDeNotaDeDebito; // Optional

    @Builder.Default
    private Boolean enviarAutomaticamenteALaSunat = false; // Optional

    @Builder.Default
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
    private List<NubefactItemDto> items = new ArrayList<>();

    private List<NubefactGuideDto> guias; // Optional
    private List<NubefactInstallmentDto> ventaAlCredito; // Optional
}
