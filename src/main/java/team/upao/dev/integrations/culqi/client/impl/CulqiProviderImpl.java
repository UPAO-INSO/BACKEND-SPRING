package team.upao.dev.integrations.culqi.client.impl;

import com.culqi.Culqi;
import com.culqi.model.ResponseCulqi;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;
import team.upao.dev.common.utils.ExpirationUtils;
import team.upao.dev.integrations.culqi.client.CulqiProvider;
import team.upao.dev.integrations.culqi.config.CulqiConfig;
import team.upao.dev.integrations.culqi.dto.CulqiOrderRequestDto;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class CulqiProviderImpl implements CulqiProvider {
    private final Culqi culqi;
    private final ObjectMapper objectMapper;

    @Value("${app.culqi.should-encrypt-payload}")
    private int shouldEncryptPayload;

    @Value("${app.culqi.rsa-public-key}")
    private String rsaPublicKey;

    @Value("${app.culqi.rsa-id}")
    private String rsaId;

    public CulqiProviderImpl(CulqiConfig culqiConfig, ObjectMapper objectMapper) {
        this.culqi = culqiConfig.init();
        this.objectMapper = objectMapper;
    }

    protected Map<String, Object> createOrderMap(CulqiOrderRequestDto orderRequest) {
        Map<String, Object> order = new HashMap<>();
        order.put("amount", orderRequest.getAmount());
        order.put("currency_code", orderRequest.getCurrencyIsoCode());
        order.put("description", orderRequest.getDescription());
        order.put("order_number", orderRequest.getOrderNumber());

        long expirationEpochSeconds = ExpirationUtils.computeExpirationEpochSeconds(orderRequest.getExpirationDate());
        order.put("expiration_date", expirationEpochSeconds);

        order.put("confirm", orderRequest.getConfirm());

        Map<String, Object> clientDetailsMap = objectMapper.convertValue(
                orderRequest.getClientDetailsRequest(),
                new TypeReference<>() {}
        );

        order.put("client_details", clientDetailsMap);

        log.info("CulqiProvider :: createOrderMap => {}", order);
        return order;
    }

    @Override
    public ResponseEntity<Object> createOrder(CulqiOrderRequestDto orderRequest) throws Exception {
        ResponseCulqi response;
        if (shouldEncryptPayload == 1) {
            response = culqi.order.create(createOrderMap(orderRequest), rsaPublicKey, rsaId);
        } else {
            response = culqi.order.create(createOrderMap(orderRequest));
        }
        return createResponseEntity(response);
    }

    private ResponseEntity<Object> createResponseEntity(ResponseCulqi response) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new ResponseEntity<>(response.getBody(), headers, response.getStatusCode());
    }
}
