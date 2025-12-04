package team.upao.dev.integrations.nubefact.mapper;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import team.upao.dev.integrations.nubefact.dto.NubefactInvoiceRequestDto;
import team.upao.dev.integrations.nubefact.dto.NubefactReceiptRequestDto;
import team.upao.dev.vouchers.dto.VoucherRequestDto;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class NubefactVoucherMapper {
    private final ObjectMapper objectMapper = new ObjectMapper();

    public VoucherRequestDto toVoucherRequestDto(NubefactInvoiceRequestDto req, String nubefactResponseBody) {
        if (req == null) return null;

        String pdfUrl = "";
        String xmlUrl = "";
        String qr = "";
        String barcode = "";

        if (nubefactResponseBody != null && !nubefactResponseBody.isBlank()) {
            try {
                JsonNode root = objectMapper.readTree(nubefactResponseBody);
                pdfUrl = getTextOrEmpty(root, "enlace_del_pdf");
                xmlUrl = getTextOrEmpty(root, "enlace_del_xml");
                qr = getTextOrEmpty(root, "cadena_para_codigo_qr");
                barcode = getTextOrEmpty(root, "codigo_de_barras");
            } catch (Exception e) {
                // parse fail -> seguir con strings vacíos
            }
        }

        return VoucherRequestDto.builder()
                .series(req.getSerie())
                .numero(req.getNumero() != null ? req.getNumero() : 0)
                .sunatTransaction(req.getSunatTransaction() != null ? req.getSunatTransaction() : 0)
                .clienteTipoDeDocumento(req.getClienteTipoDeDocumento() != null ? req.getClienteTipoDeDocumento() : 0)
                .clienteNumeroDeDocumento(req.getClienteNumeroDeDocumento())
                .clienteDenominacion(req.getClienteDenominacion())
                .clienteDireccion(req.getClienteDireccion())
                .clienteEmail(req.getClienteEmail())
                .fechaDeEmision(req.getFechaDeEmision())
                .moneda(req.getMoneda() != null ? req.getMoneda() : 0)
                .porcentajeDeIgv(req.getPorcentajeDeIgv() != null ? req.getPorcentajeDeIgv() : 18.0)
                .totalGravada(req.getTotalGravada() != null ? req.getTotalGravada() : 0.0)
                .totalIgv(req.getTotalIgv() != null ? req.getTotalIgv() : 0.0)
                .total(req.getTotal() != null ? req.getTotal() : 0.0)
                .observaciones(req.getObservaciones())
                .enviarAutomaticamenteALaSunat(req.getEnviarAutomaticamenteALaSunat())
                .enviarAutomaticamenteAlCliente(req.getEnviarAutomaticamenteAlCliente())
                .items(req.getItems())
                .pdfUrl(pdfUrl)
                .xmlUrl(xmlUrl)
                .qrCodeString(qr)
                .barCode(barcode)
                .build();
    }

    public VoucherRequestDto toVoucherRequestDtoFromReceipt(NubefactReceiptRequestDto req, String nubefactResponseBody) {
        if (req == null) return null;

        String pdfUrl = "";
        String xmlUrl = "";
        String qr = "";
        String barcode = "";

        if (nubefactResponseBody != null && !nubefactResponseBody.isBlank()) {
            try {
                JsonNode root = objectMapper.readTree(nubefactResponseBody);
                pdfUrl = getTextOrEmpty(root, "enlace_del_pdf");
                xmlUrl = getTextOrEmpty(root, "enlace_del_xml");
                qr = getTextOrEmpty(root, "cadena_para_codigo_qr");
                barcode = getTextOrEmpty(root, "codigo_de_barras");
            } catch (Exception e) {
                // parse fail -> seguir con strings vacíos
            }
        }

        return VoucherRequestDto.builder()
                .series(req.getSerie())
                .numero(req.getNumero() != null ? req.getNumero() : 0)
                .sunatTransaction(req.getSunatTransaction() != null ? req.getSunatTransaction() : 0)
                .clienteTipoDeDocumento(req.getClienteTipoDeDocumento() != null ? req.getClienteTipoDeDocumento() : 0)
                .clienteNumeroDeDocumento(req.getClienteNumeroDeDocumento())
                .clienteDenominacion(req.getClienteDenominacion())
                .clienteDireccion(req.getClienteDireccion())
                .clienteEmail(req.getClienteEmail())
                .fechaDeEmision(req.getFechaDeEmision())
                .moneda(req.getMoneda() != null ? req.getMoneda() : 0)
                .porcentajeDeIgv(req.getPorcentajeDeIgv() != null ? req.getPorcentajeDeIgv() : 18.0)
                .totalGravada(req.getTotalGravada() != null ? req.getTotalGravada() : 0.0)
                .totalIgv(req.getTotalIgv() != null ? req.getTotalIgv() : 0.0)
                .total(req.getTotal() != null ? req.getTotal() : 0.0)
                .observaciones(req.getObservaciones())
                .enviarAutomaticamenteALaSunat(req.getEnviarAutomaticamenteALaSunat())
                .enviarAutomaticamenteAlCliente(req.getEnviarAutomaticamenteAlCliente())
                .items(req.getItems())
                .pdfUrl(pdfUrl)
                .xmlUrl(xmlUrl)
                .qrCodeString(qr)
                .barCode(barcode)
                .build();
    }

    private String getTextOrEmpty(JsonNode node, String fieldName) {
        JsonNode field = node.path(fieldName);
        if (!field.isMissingNode() && !field.isNull()) {
            String text = field.asText("");
            return text != null ? text : "";
        }
        return "";
    }
}
