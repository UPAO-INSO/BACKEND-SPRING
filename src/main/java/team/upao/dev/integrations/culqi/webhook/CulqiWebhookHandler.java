package team.upao.dev.integrations.culqi.webhook;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Component
@RequiredArgsConstructor
@Slf4j
public class CulqiWebhookHandler {
    private final ObjectMapper objectMapper;

    public String readRequestBody(HttpServletRequest request) throws IOException {
        StringBuilder sb = new StringBuilder();
        try (BufferedReader reader = request.getReader()) {
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
        }
        return sb.toString();
    }

    public Object parseIncomingPayload(String rawBody, HttpServletRequest request) throws IOException {
        if (rawBody == null || rawBody.isBlank()) {
            Map<String, String[]> params = request.getParameterMap();
            if (!params.isEmpty()) {
                Map<String, Object> m = new LinkedHashMap<>();
                for (Map.Entry<String, String[]> e : params.entrySet()) {
                    String key = e.getKey();
                    String[] vals = e.getValue();
                    if (vals.length == 1) {
                        String v = URLDecoder.decode(vals[0], StandardCharsets.UTF_8);
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
        if (looksLikeJson(trimmed)) {
            try {
                return objectMapper.readValue(trimmed, Object.class);
            } catch (JsonProcessingException e) {
                log.warn("No se pudo parsear body JSON directamente, intentar heurística: {}", e.getMessage());
            }
        }

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

        if (trimmed.startsWith("{") && trimmed.contains("=")) {
            Map<String, Object> parsed = parseJavaStyleMap(trimmed);
            return parsed;
        }

        return rawBody;
    }

    public Object extractDataField(Object payloadObj, String rawBody) throws IOException {
        if (payloadObj instanceof Map) {
            Map<?, ?> map = (Map<?, ?>) payloadObj;
            if (map.containsKey("data")) {
                Object data = map.get("data");
                if (data instanceof Map || data instanceof List) {
                    return data;
                }
                if (data instanceof String) {
                    String s = ((String) data).trim();
                    if (looksLikeJson(s)) {
                        return objectMapper.readValue(s, Object.class);
                    }
                    return s;
                }
                return data;
            }
        }

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

        return payloadObj;
    }

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
        if (depth != 0) return null;
        return raw.substring(start, i);
    }

    private boolean looksLikeJson(String s) {
        String t = s.trim();
        return (t.startsWith("{") && t.endsWith("}")) || (t.startsWith("[") && t.endsWith("]"));
    }

    private Map<String, Object> parseJavaStyleMap(String s) throws IOException {
        String inner = s.trim();
        if (inner.startsWith("{") && inner.endsWith("}")) {
            inner = inner.substring(1, inner.length() - 1);
        }
        Map<String, Object> result = new LinkedHashMap<>();
        int len = inner.length();
        int i = 0;
        while (i < len) {
            while (i < len && Character.isWhitespace(inner.charAt(i))) i++;
            int keyStart = i;
            while (i < len && inner.charAt(i) != '=') i++;
            if (i >= len) break;
            String key = inner.substring(keyStart, i).trim();
            i++;
            while (i < len && Character.isWhitespace(inner.charAt(i))) i++;
            if (i >= len) {
                result.put(key, null);
                break;
            }
            char c = inner.charAt(i);
            String valueStr;
            if (c == '{' || c == '[') {
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
                int j = i;
                while (j < len && inner.charAt(j) != ',') j++;
                valueStr = inner.substring(i, j).trim();
                i = j;
            }
            if (i < len && inner.charAt(i) == ',') i++;

            Object valueObj;
            if (valueStr == null || valueStr.equalsIgnoreCase("null")) {
                valueObj = null;
            } else if (looksLikeJson(valueStr)) {
                valueObj = objectMapper.readValue(valueStr, Object.class);
            } else if ((valueStr.startsWith("\"") && valueStr.endsWith("\"")) || (valueStr.startsWith("'") && valueStr.endsWith("'"))) {
                valueObj = valueStr.substring(1, valueStr.length() - 1);
            } else {
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
                        valueObj = valueStr;
                    }
                }
            }
            result.put(key, valueObj);
        }
        return result;
    }
}
