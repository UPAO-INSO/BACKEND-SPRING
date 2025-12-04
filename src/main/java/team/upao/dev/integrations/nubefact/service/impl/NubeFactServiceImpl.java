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
import team.upao.dev.vouchers.service.VoucherService;

@Slf4j
@Service
@RequiredArgsConstructor
public class NubeFactServiceImpl implements NubefactService {
    private final NubefactClient nubefactClient;
    private final NubefactVoucherMapper nubefactVoucherMapper;
    private final VoucherService voucherService;

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

    public ResponseEntity<String> sendInvoice(NubefactInvoiceRequestDto requestBody) {
        NubefactInvoiceRequestDto requestToSend = normalize(requestBody);
        log.info("NubefactClient - Sending invoice request: {}", requestBody);
        ResponseEntity<String> response = nubefactClient.sendInvoice(requestBody);

        if (response != null && response.getStatusCode().is2xxSuccessful()) {
            try {
                VoucherRequestDto voucherReq = nubefactVoucherMapper.toVoucherRequestDto(requestBody, response.getBody());
                voucherService.create(voucherReq);
                log.info("Voucher created from Nubefact invoice (serie={} numero={})", voucherReq.getSeries(), voucherReq.getNumero());
            } catch (Exception e) {
                log.warn("Failed to create voucher from nubefact invoice response: {}", e.getMessage());
            }
        } else {
            log.warn("Nubefact sendInvoice did not return 2xx: {}", response != null ? response.getStatusCode() : "null response");
        }

        return response;
    }

    @Override
    public ResponseEntity<String> sendReceipt(NubefactReceiptRequestDto requestBody) {
        NubefactReceiptRequestDto requestToSend = normalize(requestBody);
        log.info("NubefactClient - Sending receipt request: {}", requestBody);
        ResponseEntity<String> response = nubefactClient.sendReceipt(requestBody);

        if (response != null && response.getStatusCode().is2xxSuccessful()) {
            try {
                VoucherRequestDto voucherReq = nubefactVoucherMapper.toVoucherRequestDtoFromReceipt(requestBody, response.getBody());
                voucherService.create(voucherReq);
                log.info("Voucher created from Nubefact receipt (serie={} numero={})", voucherReq.getSeries(), voucherReq.getNumero());
            } catch (Exception e) {
                log.warn("Failed to create voucher from nubefact receipt response: {}", e.getMessage());
            }
        } else {
            log.warn("Nubefact sendReceipt did not return 2xx: {}", response != null ? response.getStatusCode() : "null response");
        }

        return response;
    }
}
