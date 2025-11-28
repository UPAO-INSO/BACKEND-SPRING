package team.upao.dev.integrations.culqi.service.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import team.upao.dev.integrations.culqi.client.CulqiProvider;
import team.upao.dev.integrations.culqi.dto.CulqiOrderRequestDto;
import team.upao.dev.integrations.culqi.service.PaymentService;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {
    private final CulqiProvider culqiProvider;
    private final ObjectMapper objectMapper;

    @Override
    public ResponseEntity<Object> createOrder(CulqiOrderRequestDto orderRequest) {
        try {
            ResponseEntity<Object> providerResponse = culqiProvider.createOrder(orderRequest);
            HttpStatusCode status = providerResponse.getStatusCode();
            Object rawBody = providerResponse.getBody();
            Object body = parseBody(rawBody);

            if (status.is2xxSuccessful()) {
                log.info("Orden creada correctamente => status: {}, body: {}", status.value(), body);
                return new ResponseEntity<>(body, status);
            } else {
                Map<String, Object> errorPayload = new HashMap<>();
                errorPayload.put("status", status.value());
                errorPayload.put("error", body != null ? body : rawBody);
                log.warn("Error al crear orden en Culqi => {}", errorPayload);
                return new ResponseEntity<>(errorPayload, status);
            }
        } catch (Exception e) {
            log.error("Error al comunicarse con Culqi", e);
            Map<String, Object> gatewayError = new HashMap<>();
            gatewayError.put("message", "Error de comunicación con el proveedor de pagos: " + e);
            gatewayError.put("detail", e.getMessage());
            return new ResponseEntity<>(gatewayError, HttpStatus.BAD_GATEWAY);
        }
    }

    private Object parseBody(Object rawBody) {
        if (rawBody == null) return null;
        if (rawBody instanceof String) {
            String bodyStr = ((String) rawBody).trim();
            if (bodyStr.isEmpty()) return bodyStr;
            try {
                return objectMapper.readValue(bodyStr, new TypeReference<Map<String, Object>>() {});
            } catch (Exception ex) {
                log.debug("No se pudo parsear body como JSON, se devuelve String crudo", ex);
                return bodyStr;
            }
        }
        return rawBody;
    }
}
