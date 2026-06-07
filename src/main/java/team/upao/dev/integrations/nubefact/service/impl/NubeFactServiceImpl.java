package team.upao.dev.integrations.nubefact.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import team.upao.dev.integrations.nubefact.client.NubefactClient;
import team.upao.dev.integrations.nubefact.dto.NubefactInvoiceRequestDto;
import team.upao.dev.integrations.nubefact.dto.NubefactReceiptRequestDto;
import team.upao.dev.integrations.nubefact.mapper.NubefactVoucherMapper;
import team.upao.dev.integrations.nubefact.service.NubefactService;
import team.upao.dev.vouchers.dto.VoucherRequestDto;
import team.upao.dev.vouchers.repository.IVoucherRepository;
import team.upao.dev.vouchers.service.VoucherService;

@Slf4j
@Service
@RequiredArgsConstructor
public class NubeFactServiceImpl implements NubefactService {

    private final NubefactClient        nubefactClient;
    private final NubefactVoucherMapper nubefactVoucherMapper;
    private final VoucherService        voucherService;
    private final IVoucherRepository    voucherRepository;

    // ── Normalización ────────────────────────────────────────────────

    private NubefactInvoiceRequestDto normalize(NubefactInvoiceRequestDto req) {
        if (req == null) return null;
        req.applyDefaults();
        return req;
    }

    private NubefactReceiptRequestDto normalize(NubefactReceiptRequestDto req) {
        if (req == null) return null;
        req.applyDefaults();
        return req;
    }

    // ── Auto-numeración ──────────────────────────────────────────────

    /**
     * Retorna el siguiente número disponible para la serie dada.
     * Consulta el máximo número ya guardado en la DB y suma 1.
     * Si no hay comprobantes previos para esa serie, empieza en 1.
     */
    private int nextNumber(String series) {
        return voucherRepository.findMaxNumberBySeries(series)
                .map(max -> max + 1)
                .orElse(1);
    }

    // ── Envío ────────────────────────────────────────────────────────

    @Override
    public ResponseEntity<String> sendInvoice(NubefactInvoiceRequestDto requestBody) {
        normalize(requestBody);

        // Auto-incrementar el número para evitar duplicados en Nubefact
        int numero = nextNumber(requestBody.getSerie());
        requestBody.setNumero(numero);

        // Código único: identifica de forma unívoca este comprobante en Nubefact
        // Formato: serie-numero-timestamp para garantizar unicidad absoluta
        String codigoUnico = requestBody.getSerie() + "-" + numero + "-" + System.currentTimeMillis();
        requestBody.setCodigoUnico(codigoUnico);

        log.info("Enviando factura a Nubefact: serie={}, numero={}, codigoUnico={}", requestBody.getSerie(), numero, codigoUnico);

        ResponseEntity<String> response = nubefactClient.sendInvoice(requestBody);

        if (response != null && response.getStatusCode().is2xxSuccessful()) {
            try {
                VoucherRequestDto voucherReq = nubefactVoucherMapper.toVoucherRequestDto(requestBody, response.getBody());
                // Asociar el pago automáticamente si viene el paymentId en el request
                if (requestBody.getPaymentId() != null) {
                    voucherReq.setPaymentId(requestBody.getPaymentId());
                }
                voucherService.create(voucherReq);
                log.info("Voucher creado desde factura Nubefact (serie={} numero={}, paymentId={})",
                        voucherReq.getSeries(), voucherReq.getNumero(), voucherReq.getPaymentId());
            } catch (Exception e) {
                log.warn("No se pudo guardar el voucher de factura: {}", e.getMessage());
            }
        } else {
            log.warn("Nubefact sendInvoice no retornó 2xx: {}", response != null ? response.getStatusCode() : "null");
        }

        return response;
    }

    @Override
    public ResponseEntity<String> sendReceipt(NubefactReceiptRequestDto requestBody) {
        normalize(requestBody);

        // Auto-incrementar el número para evitar duplicados en Nubefact
        int numero = nextNumber(requestBody.getSerie());
        requestBody.setNumero(numero);

        // Código único requerido por Nubefact (codigo: 21 si falta)
        String codigoUnico = requestBody.getSerie() + "-" + numero + "-" + System.currentTimeMillis();
        requestBody.setCodigoUnico(codigoUnico);

        log.info("Enviando boleta a Nubefact: serie={}, numero={}, codigoUnico={}", requestBody.getSerie(), numero, codigoUnico);

        ResponseEntity<String> response = nubefactClient.sendReceipt(requestBody);

        if (response != null && response.getStatusCode().is2xxSuccessful()) {
            try {
                VoucherRequestDto voucherReq = nubefactVoucherMapper.toVoucherRequestDtoFromReceipt(requestBody, response.getBody());
                // Asociar el pago automáticamente si viene el paymentId en el request
                if (requestBody.getPaymentId() != null) {
                    voucherReq.setPaymentId(requestBody.getPaymentId());
                }
                voucherService.create(voucherReq);
                log.info("Voucher creado desde boleta Nubefact (serie={} numero={}, paymentId={})",
                        voucherReq.getSeries(), voucherReq.getNumero(), voucherReq.getPaymentId());
            } catch (Exception e) {
                log.warn("No se pudo guardar el voucher de boleta: {}", e.getMessage());
            }
        } else {
            log.warn("Nubefact sendReceipt no retornó 2xx: {}", response != null ? response.getStatusCode() : "null");
        }

        return response;
    }
}
