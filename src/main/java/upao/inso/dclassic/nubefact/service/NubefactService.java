package upao.inso.dclassic.nubefact.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import upao.inso.dclassic.nubefact.client.NubefactClient;
import upao.inso.dclassic.nubefact.dto.NubefactInvoiceRequestDto;

@Slf4j
@Service
@RequiredArgsConstructor
public class NubefactService {

    private final NubefactClient nubefactClient;

    public ResponseEntity<String> sendInvoice(NubefactInvoiceRequestDto requestBody) {
        log.info("NubefactClient - Sending invoice request: {}", requestBody);
        return nubefactClient.sendInvoice(requestBody);
    }

//    public String sendCreditNote(String accessToken, String requestBody) {
//        return nubefactClient.sendCreditNote(accessToken, requestBody);
//    }
//
//    public String sendDebitNote(String accessToken, String requestBody) {
//        return nubefactClient.sendDebitNote(accessToken, requestBody);
//    }
//
//    public String sendGuide(String accessToken, String requestBody) {
//        return nubefactClient.sendGuide(accessToken, requestBody);
//    }
}
