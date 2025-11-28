package team.upao.dev.payments.culqi.controller;

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
import team.upao.dev.payments.culqi.dto.CulqiOrderRequestDto;
import team.upao.dev.payments.culqi.service.PaymentService;

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
public class PaymentController {
    private final PaymentService paymentService;
    private final SimpMessagingTemplate messagingTemplate;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @PostMapping("/createOrder")
    public ResponseEntity<Object> createOrder(@RequestBody @Valid CulqiOrderRequestDto orderRequest) throws Exception {
        return paymentService.createOrder(orderRequest);
    }

    @PostMapping("/confirmOrder/{orderId}")
    public ResponseEntity<Object> confirmOrder(@PathVariable String orderId) throws Exception {
        return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body("Implementar confirmOrder en CulqiProvider");
    }

    @PostMapping("/webhook")
    public ResponseEntity<Void> webhook(HttpServletRequest request) {
        String rawBody = null;
        try {
            rawBody = readRequestBody(request);
            Object payloadObj = parseIncomingPayload(rawBody, request);
            log.info("Webhook Culqi recibido (parsed) => {}", payloadObj);

            // Extraer campo 'data' si existe; si no, reenviar payload completo
            Object dataObj = extractDataField(payloadObj, rawBody);

            log.info("Enviando por websocket el objeto 'data' => {}", dataObj);
            messagingTemplate.convertAndSend("/topic/culqi-order", dataObj);
        } catch (Exception e) {
            log.error("Error procesando webhook Culqi, se reenvía cuerpo crudo", e);
            // En caso de error, reenviamos el cuerpo crudo para no perder el evento
            messagingTemplate.convertAndSend("/topic/culqi-order", rawBody != null ? rawBody : "");
        }
        return ResponseEntity.ok().build();
    }

    // Lee el cuerpo crudo del request
    private String readRequestBody(HttpServletRequest request) throws IOException {
        StringBuilder sb = new StringBuilder();
        try (BufferedReader reader = request.getReader()) {
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
        }
        return sb.toString();
    }

    // Extrae especificamente el campo 'data' del payload (si es posible)
    private Object extractDataField(Object payloadObj, String rawBody) throws IOException {
        if (payloadObj instanceof Map) {
            Map<?, ?> map = (Map<?, ?>) payloadObj;
            if (map.containsKey("data")) {
                Object data = map.get("data");
                // si ya está como Map/List u otro objeto, devolverlo
                if (data instanceof Map || data instanceof List) {
                    return data;
                }
                // si es String que contiene JSON, parsearlo
                if (data instanceof String) {
                    String s = ((String) data).trim();
                    if (looksLikeJson(s)) {
                        return objectMapper.readValue(s, Object.class);
                    }
                    // fallback: devolver el string
                    return s;
                }
                return data;
            }
        }

        // Si no es Map o no contiene data, intentar extraer desde el rawBody
        if (rawBody != null) {
            String extracted = extractDataFromRawString(rawBody);
            if (extracted != null) {
                String t = extracted.trim();
                if (looksLikeJson(t)) {
                    return objectMapper.readValue(t, Object.class);
                }
                return t;
            }
        }

        // fallback: devolver payload completo
        return payloadObj;
    }

    // Intenta encontrar `data={...}` en un raw string y devolver el substring JSON
    private String extractDataFromRawString(String raw) {
        int idx = raw.indexOf("data=");
        if (idx < 0) return null;
        int start = raw.indexOf('{', idx);
        if (start < 0) return null;
        int len = raw.length();
        int depth = 0;
        int i = start;
        while (i < len) {
            char c = raw.charAt(i);
            if (c == '{') depth++; else if (c == '}') depth--;
            i++;
            if (depth == 0) break;
        }
        if (depth != 0) return null; // no balanceado
        return raw.substring(start, i);
    }

    // Intenta parsear el payload intentando varias estrategias
    private Object parseIncomingPayload(String rawBody, HttpServletRequest request) throws IOException {
        if (rawBody == null || rawBody.isBlank()) {
            // intentar parámetros form
            Map<String, String[]> params = request.getParameterMap();
            if (!params.isEmpty()) {
                Map<String, Object> m = new LinkedHashMap<>();
                for (Map.Entry<String, String[]> e : params.entrySet()) {
                    String key = e.getKey();
                    String[] vals = e.getValue();
                    if (vals.length == 1) {
                        String v = URLDecoder.decode(vals[0], StandardCharsets.UTF_8);
                        // si el valor es JSON, parsear
                        if (looksLikeJson(v)) {
                            m.put(key, objectMapper.readValue(v, Object.class));
                        } else {
                            m.put(key, v);
                        }
                    } else {
                        m.put(key, Arrays.asList(vals));
                    }
                }
                return m;
            }
            return Collections.emptyMap();
        }

        String trimmed = rawBody.trim();
        // si parece JSON, devolver parseado
        if (looksLikeJson(trimmed)) {
            try {
                return objectMapper.readValue(trimmed, Object.class);
            } catch (JsonProcessingException e) {
                // seguir a heurística siguiente
                log.warn("No se pudo parsear body JSON directamente, intentar heurística: {}", e.getMessage());
            }
        }

        // si es form-urlencoded (key=value&...), parsear
        if (trimmed.contains("=") && trimmed.contains("&")) {
            Map<String, Object> form = new LinkedHashMap<>();
            String[] pairs = trimmed.split("&");
            for (String p : pairs) {
                int idx = p.indexOf('=');
                if (idx > 0) {
                    String k = URLDecoder.decode(p.substring(0, idx), StandardCharsets.UTF_8);
                    String v = URLDecoder.decode(p.substring(idx + 1), StandardCharsets.UTF_8);
                    if (looksLikeJson(v)) {
                        form.put(k, objectMapper.readValue(v, Object.class));
                    } else {
                        form.put(k, v);
                    }
                }
            }
            return form;
        }

        // si es un string con formato tipo Java Map.toString: {object=event, id=..., data={"object":"order"...}}
        if (trimmed.startsWith("{") && trimmed.contains("=")) {
            Map<String, Object> parsed = parseJavaStyleMap(trimmed);
            return parsed;
        }

        // fallback: intentar devolver como string
        return rawBody;
    }

    private boolean looksLikeJson(String s) {
        String t = s.trim();
        return (t.startsWith("{") && t.endsWith("}")) || (t.startsWith("[") && t.endsWith("]"));
    }

    // Parser simple para strings en formato {key=value, key2=value2, key3={...}}
    private Map<String, Object> parseJavaStyleMap(String s) throws IOException {
        String inner = s.trim();
        if (inner.startsWith("{") && inner.endsWith("}")) {
            inner = inner.substring(1, inner.length() - 1);
        }
        Map<String, Object> result = new LinkedHashMap<>();
        int len = inner.length();
        int i = 0;
        while (i < len) {
            // saltar espacios
            while (i < len && Character.isWhitespace(inner.charAt(i))) i++;
            // leer key
            int keyStart = i;
            while (i < len && inner.charAt(i) != '=') i++;
            if (i >= len) break;
            String key = inner.substring(keyStart, i).trim();
            i++; // saltar '='
            // leer value
            while (i < len && Character.isWhitespace(inner.charAt(i))) i++;
            if (i >= len) {
                result.put(key, null);
                break;
            }
            char c = inner.charAt(i);
            String valueStr;
            if (c == '{' || c == '[') {
                // capturar bloque anidado respetando pares
                char open = c;
                char close = (open == '{') ? '}' : ']';
                int depth = 0;
                int j = i;
                while (j < len) {
                    char cc = inner.charAt(j);
                    if (cc == open) depth++; else if (cc == close) depth--;
                    j++;
                    if (depth == 0) break;
                }
                valueStr = inner.substring(i, Math.min(j, len)).trim();
                i = j;
            } else {
                // valor sencillo hasta la siguiente coma a nivel superior
                int j = i;
                while (j < len && inner.charAt(j) != ',') j++;
                valueStr = inner.substring(i, j).trim();
                i = j;
            }
            // saltar la coma
            if (i < len && inner.charAt(i) == ',') i++;

            // procesar valueStr
            Object valueObj;
            if (valueStr == null || valueStr.equalsIgnoreCase("null")) {
                valueObj = null;
            } else if (looksLikeJson(valueStr)) {
                // si es JSON parsearlo
                valueObj = objectMapper.readValue(valueStr, Object.class);
            } else if ((valueStr.startsWith("\"") && valueStr.endsWith("\"")) || (valueStr.startsWith("'") && valueStr.endsWith("'"))) {
                valueObj = valueStr.substring(1, valueStr.length() - 1);
            } else {
                // intentar detectar booleano o numero
                if (valueStr.equalsIgnoreCase("true") || valueStr.equalsIgnoreCase("false")) {
                    valueObj = Boolean.parseBoolean(valueStr);
                } else {
                    try {
                        if (valueStr.contains(".")) {
                            valueObj = Double.parseDouble(valueStr);
                        } else {
                            valueObj = Long.parseLong(valueStr);
                        }
                    } catch (NumberFormatException ex) {
                        // fallback a string
                        valueObj = valueStr;
                    }
                }
            }
            result.put(key, valueObj);
        }
        return result;
    }
}
