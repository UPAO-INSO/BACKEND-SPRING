package team.upao.dev.vouchers.dto;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import team.upao.dev.integrations.nubefact.dto.NubefactItemDto;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor @AllArgsConstructor
public class VoucherRequestDto {
    @JsonProperty("serie")
    public String series;

    public int numero;

    @JsonProperty("sunat_transaction")
    public int sunatTransaction;

    @JsonProperty("cliente_tipo_de_documento")
    public int clienteTipoDeDocumento;

    @JsonProperty("cliente_numero_de_documento")
    public String clienteNumeroDeDocumento;

    @JsonProperty("cliente_denominacion")
    public String clienteDenominacion;

    @JsonProperty("cliente_direccion")
    public String clienteDireccion;

    @JsonProperty("cliente_email")
    public String clienteEmail;

    @JsonProperty("fecha_de_emision")
    public String fechaDeEmision;

    public Integer moneda;

    @JsonProperty("porcentaje_de_igv")
    public Double porcentajeDeIgv;

    @JsonProperty("total_gravada")
    public Double totalGravada;

    @JsonProperty("total_igv")
    public Double totalIgv;

    @JsonProperty("total")
    public Double total;

    public String observaciones;

    @JsonProperty("enviar_automaticamente_a_la_sunat")
    public boolean enviarAutomaticamenteALaSunat;

    @JsonProperty("enviar_automaticamente_al_cliente")
    public boolean enviarAutomaticamenteAlCliente;

    public List<NubefactItemDto> items;

    @JsonProperty("pdf_url")
    public String pdfUrl;

    @JsonProperty("xml_url")
    public String xmlUrl;

    @JsonProperty("qr_code_string")
    public String qrCodeString;

    @JsonProperty("bar_code")
    public String barCode;

    private Long paymentId;
}
