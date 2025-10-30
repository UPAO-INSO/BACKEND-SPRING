package team.upao.dev.nubefact.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter @Setter
@AllArgsConstructor @NoArgsConstructor
@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class NubefactInvoiceRequestDto {
    @Builder.Default
    private String operacion = "generar_comprobante";
    @NotBlank
    private Integer tipoDeComprobante;
    @NotBlank
    private String serie;
    @NotBlank
    private Integer numero;
    @NotBlank
    private Integer sunatTransaction;
    @NotBlank
    private Integer clienteTipoDeDocumento;
    @NotBlank
    private String clienteNumeroDeDocumento;
    @NotBlank
    private String clienteDenominacion;
    @NotBlank
    private String clienteDireccion;
    @NotBlank
    @Email
    private String clienteEmail;
    @Email
    private String clienteEmail1; // Optional
    @Email
    private String clienteEmail2; // Optional
    @NotBlank
    private String fechaDeEmision;
    private String fechaDeVencimiento; // Optional
    @NotBlank
    private Integer moneda;
    private String tipoDeCambio; // Optional
    @NotBlank
    private Double porcentajeDeIgv;

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

    private Boolean detraccion = false; // Optional
    @Builder.Default
    private String observaciones = "";

    private String documentoQueSeModificaTipo; // Optional
    private String documentoQueSeModificaSerie; // Optional
    private String documentoQueSeModificaNumero;
    private String tipoDeNotaDeCredito; // Optional
    private String tipoDeNotaDeDebito; // Optional

    private Boolean enviarAutomaticamenteALaSunat = false; // Optional
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
