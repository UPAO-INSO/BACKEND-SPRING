package team.upao.dev.integrations.culqi.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import team.upao.dev.integrations.culqi.dto.CulqiOrderRequestDto;
import team.upao.dev.integrations.culqi.service.CulqiService;
import team.upao.dev.integrations.culqi.webhook.CulqiWebhookHandler;
import team.upao.dev.integrations.culqi.webhook.CulqiWebhookProcessor;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Slf4j
@RestController
@RequestMapping("/payments/culqi")
@RequiredArgsConstructor
@Validated
@Tag(name = "Pagos Culqi", description = "API para procesar pagos con Culqi (Yape y otras billeteras digitales)")
public class CulqiController {
    private final CulqiService culqiService;
    private final SimpMessagingTemplate messagingTemplate;
    private final CulqiWebhookHandler culqiWebhookHandler;
    private final CulqiWebhookProcessor culqiWebhookProcessor;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @PostMapping("/createOrder")
    public ResponseEntity<Object> createOrder(@RequestBody @Valid CulqiOrderRequestDto orderRequest) throws Exception {
        return culqiService.createOrder(orderRequest);
    }

    @PostMapping("/confirmOrder/{orderId}")
    public ResponseEntity<Object> confirmOrder(@PathVariable String orderId) throws Exception {
        return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body("Implementar confirmOrder en CulqiProvider");
    }

    @PostMapping("/webhook")
    public ResponseEntity<Void> webhook(HttpServletRequest request) {
        String rawBody = null;
        try {
            rawBody = culqiWebhookHandler.readRequestBody(request);
            Object payloadObj = culqiWebhookHandler.parseIncomingPayload(rawBody, request);

            Object dataObj = culqiWebhookHandler.extractDataField(payloadObj, rawBody);
            culqiWebhookProcessor.process(dataObj, rawBody);
            messagingTemplate.convertAndSend("/topic/culqi-order", dataObj);
        } catch (Exception e) {
            log.error("Error procesando webhook Culqi, se reenvía cuerpo crudo", e);
            messagingTemplate.convertAndSend("/topic/culqi-order", Objects.requireNonNullElse(rawBody, ""));
        }
        return ResponseEntity.ok().build();
    }
}
