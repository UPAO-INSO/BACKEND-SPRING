package team.upao.dev.payments.culqi.client;

import java.time.Duration;
import java.util.function.Supplier;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import lombok.extern.slf4j.Slf4j;
import team.upao.dev.payments.culqi.config.CulqiProperties;
import team.upao.dev.payments.culqi.dto.CulqiChargeRequest;
import team.upao.dev.payments.culqi.dto.CulqiChargeResponse;
import team.upao.dev.payments.culqi.dto.CulqiTokenResponse;
import team.upao.dev.payments.culqi.dto.CulqiYapeTokenRequest;

@Slf4j
@Component
public class CulqiClient {
    private final RestTemplate restTemplate;
    private final CulqiProperties properties;

    public CulqiClient(RestTemplateBuilder restTemplateBuilder, CulqiProperties properties) {
        this.properties = properties;
    Supplier<ClientHttpRequestFactory> requestFactorySupplier = () -> {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout((int) Duration.ofSeconds(properties.getTimeoutConnectSeconds()).toMillis());
        factory.setReadTimeout((int) Duration.ofSeconds(properties.getTimeoutReadSeconds()).toMillis());
        return factory;
    };

    this.restTemplate = restTemplateBuilder
        .requestFactory(requestFactorySupplier)
        .build();
    }

    public CulqiTokenResponse createYapeToken(CulqiYapeTokenRequest request) {
        String endpoint = properties.getSecureBaseUrl() + "/v2/tokens/yape";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(properties.getPublicKey());
        return executePost(endpoint, request, headers, CulqiTokenResponse.class);
    }

    public CulqiChargeResponse createCharge(CulqiChargeRequest request) {
        String endpoint = properties.getBaseUrl() + "/v2/charges";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(properties.getSecretKey());
        return executePost(endpoint, request, headers, CulqiChargeResponse.class);
    }

    private <T> T executePost(String url, Object requestBody, HttpHeaders headers, Class<T> responseType) {
        try {
            HttpEntity<Object> entity = new HttpEntity<>(requestBody, headers);
            ResponseEntity<T> response = restTemplate.exchange(url, HttpMethod.POST, entity, responseType);
            return response.getBody();
        } catch (HttpStatusCodeException ex) {
            log.error("Culqi API error: status={}, body={}", ex.getStatusCode(), ex.getResponseBodyAsString());
            throw new IllegalArgumentException("Culqi API error: " + ex.getResponseBodyAsString(), ex);
        } catch (RestClientException ex) {
            log.error("Error calling Culqi API", ex);
            throw new IllegalStateException("No se pudo conectar con Culqi: " + ex.getMessage(), ex);
        }
    }
}
