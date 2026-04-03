package team.upao.dev.payments.mapper;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import team.upao.dev.customers.mapper.CustomerMapper;
import team.upao.dev.orders.mapper.OrderMapper;
import team.upao.dev.payments.dto.PaymentRequestDto;
import team.upao.dev.payments.dto.PaymentResponseDto;
import team.upao.dev.payments.enums.PaymentType;
import team.upao.dev.payments.model.PaymentModel;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class PaymentMapper {
    private final ObjectMapper objectMapper;
    private final OrderMapper orderMapper;
    private final CustomerMapper customerMapper;

    private static String asString(Object o) {
        return o == null ? null : String.valueOf(o);
    }

    private static Integer asInteger(Object o) {
        if (o == null) return null;
        if (o instanceof Number) return ((Number) o).intValue();
        try { return Integer.parseInt(String.valueOf(o)); } catch (Exception e) { return null; }
    }

    private static BigDecimal asBigDecimal(Object o) {
        if (o == null) return null;
        try {
            if (o instanceof Number) return BigDecimal.valueOf(((Number) o).doubleValue());
            return new BigDecimal(String.valueOf(o));
        } catch (Exception e) {
            return null;
        }
    }

    private static Instant asInstantEpoch(Object o) {
        if (o == null) return null;
        try {
            if (o instanceof Number) return Instant.ofEpochSecond(((Number) o).longValue());
            long v = Long.parseLong(String.valueOf(o));
            return Instant.ofEpochSecond(v);
        } catch (Exception e) {
            return null;
        }
    }

    public PaymentRequestDto toPaymentRequestDto(Object body, Object rawBody) {
        if (body == null) return null;
        Map<String, Object> map = objectMapper.convertValue(body, new TypeReference<>() {});

        String provider = asString(map.get("provider"));
        String externalId = asString(map.get("id"));
        Integer amount = asInteger(map.get("amount"));
        String currencyCode = asString(map.get("currency_code"));
        String description = asString(map.get("description"));
        String orderNumber = asString(map.get("order_number"));
        String state = asString(map.get("state"));
        String qr = asString(map.get("qr"));
        String urlPe = asString(map.get("url_pe"));

        BigDecimal totalFee = asBigDecimal(map.get("total_fee"));
        BigDecimal netAmount = asBigDecimal(map.get("net_amount"));

        Instant creationDate = asInstantEpoch(map.get("creation_date"));
        Instant expirationDate = asInstantEpoch(map.get("expiration_date"));
        Instant updatedAt = asInstantEpoch(map.get("updated_at"));
        Instant paidAt = asInstantEpoch(map.get("paid_at"));
        Object metadataObj = map.get("metadata");
        PaymentRequestDto.Metadata metadata = null;
        if (metadataObj != null) {
            metadata = objectMapper.convertValue(metadataObj, PaymentRequestDto.Metadata.class);
        }
        String rawResponse;
        if (rawBody instanceof String) {
            rawResponse = (String) rawBody;
        } else {
            try {
                rawResponse = objectMapper.writeValueAsString(body);
            } catch (Exception e) {
                rawResponse = String.valueOf(body);
            }
        }

        return PaymentRequestDto.builder()
                .provider(provider != null ? provider : "culqi")
                .externalId(externalId)
                .amount(amount)
                .currencyCode(currencyCode != null ? currencyCode : "PEN")
                .description(description != null ? description : "")
                .orderId(UUID.fromString(orderNumber))
                .customerId(metadata != null ? metadata.getCustomerId() : null)
                .paymentType(PaymentType.MOBILE_WALLET)
                .state(state != null ? state : "pending")
                .totalFee(totalFee)
                .netAmount(netAmount)
                .qr(qr != null ? qr : "")
                .urlPe(urlPe != null ? urlPe : "")
                .creationDate(creationDate)
                .expirationDate(expirationDate)
                .updatedAt(updatedAt)
                .paidAt(paidAt)
                .rawResponse(rawResponse != null ? rawResponse : "")
                .build();
    }

    public PaymentResponseDto toDto(PaymentModel paymentModel) {
        return PaymentResponseDto.builder()
                .id(paymentModel.getId())
                .provider(paymentModel.getProvider())
                .externalId(paymentModel.getExternalId())
                .amount(paymentModel.getAmount())
                .currencyCode(paymentModel.getCurrencyCode())
                .description(paymentModel.getDescription())
                .order(orderMapper.toDto(paymentModel.getOrder()))
                .customer(customerMapper.toDto(paymentModel.getCustomer()))
                .paymentType(paymentModel.getPaymentType())
                .state(paymentModel.getState())
                .totalFee(paymentModel.getTotalFee())
                .netAmount(paymentModel.getNetAmount())
                .qr(paymentModel.getQr())
                .urlPe(paymentModel.getUrlPe())
                .creationDate(paymentModel.getCreationDate())
                .updatedAt(paymentModel.getUpdatedAt())
                .expirationDate(paymentModel.getExpirationDate())
                .paidAt(paymentModel.getPaidAt())
                .rawResponse(paymentModel.getRawResponse())
                .createdAt(paymentModel.getCreatedAt())
                .updatedAt(paymentModel.getUpdatedAt())
                .modifiedAt(paymentModel.getModifiedAt())
                .build();
    }

    public List<PaymentResponseDto> toDto(List<PaymentModel> paymentModels) {
        return paymentModels.stream()
                .map(this::toDto)
                .toList();
    }

    public PaymentModel toModel(PaymentRequestDto paymentRequestDto) {
        return PaymentModel.builder()
                .provider(paymentRequestDto.getProvider())
                .externalId(paymentRequestDto.getExternalId())
                .amount(paymentRequestDto.getAmount())
                .currencyCode(paymentRequestDto.getCurrencyCode())
                .description(paymentRequestDto.getDescription())
                .order(null)
                .customer(null)
                .paymentType(paymentRequestDto.getPaymentType())
                .state(paymentRequestDto.getState())
                .totalFee(paymentRequestDto.getTotalFee())
                .netAmount(paymentRequestDto.getNetAmount())
                .qr(paymentRequestDto.getQr())
                .urlPe(paymentRequestDto.getUrlPe())
                .creationDate(paymentRequestDto.getCreationDate())
                .updatedAt(paymentRequestDto.getUpdatedAt())
                .expirationDate(paymentRequestDto.getExpirationDate())
                .paidAt(paymentRequestDto.getPaidAt())
                .rawResponse(paymentRequestDto.getRawResponse())
                .build();
    }
}
