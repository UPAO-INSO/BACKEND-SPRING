package team.upao.dev.nubefact.client;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import team.upao.dev.nubefact.dto.NubefactInvoiceRequestDto;

@Slf4j
@Component
public class NubefactClient {
    private final RestTemplate restTemplate;

    @Value("${nubefact.api.url}")
    private String apiUrl;

    @Value("${nubefact.api.token}")
    private String apiToken;

    public NubefactClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public ResponseEntity<String> sendInvoice(NubefactInvoiceRequestDto invoiceRequest) {
        return sendToNubefact(invoiceRequest);
    }

    public ResponseEntity<String> sendCreditNote(Object creditNoteRequest) {
        return sendToNubefact(creditNoteRequest);
    }

    public ResponseEntity<String> sendDebitNote(Object debitNoteRequest) {
        return sendToNubefact(debitNoteRequest);
    }

    public ResponseEntity<String> sendGuide(Object guideRequest) {
        return sendToNubefact(guideRequest);
    }

    private ResponseEntity<String> sendToNubefact(Object request) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set(HttpHeaders.AUTHORIZATION, "Token token=\"" + apiToken + "\"");
        HttpEntity<Object> entity = new HttpEntity<>(request, headers);
        ResponseEntity<String> response = restTemplate.exchange(apiUrl, HttpMethod.POST, entity, String.class);
        // Procesar respuesta para detectar errores de Nubefact
        if (response.getBody() != null && response.getBody().contains("\"errors\"")) {
            // Puedes lanzar una excepción personalizada aquí si lo deseas
            log.error("Nubefact error: {}", response.getBody());
        }
        return response;
    }
}
