package team.upao.dev.integrations.nubefact.service;

import org.springframework.http.ResponseEntity;
import team.upao.dev.integrations.nubefact.dto.NubefactInvoiceRequestDto;
import team.upao.dev.integrations.nubefact.dto.NubefactReceiptRequestDto;

public interface NubefactService {
    ResponseEntity<String> sendInvoice(NubefactInvoiceRequestDto requestBody);
    ResponseEntity<String> sendReceipt(NubefactReceiptRequestDto requestBody);
}
