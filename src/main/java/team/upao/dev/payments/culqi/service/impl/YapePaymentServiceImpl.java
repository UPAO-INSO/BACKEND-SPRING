package team.upao.dev.payments.culqi.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import team.upao.dev.orders.dto.ChangeOrderStatusDto;
import team.upao.dev.orders.enums.OrderStatus;
import team.upao.dev.orders.model.OrderModel;
import team.upao.dev.orders.service.OrderService;
import team.upao.dev.payments.culqi.client.CulqiClient;
import team.upao.dev.payments.culqi.config.CulqiProperties;
import team.upao.dev.payments.culqi.dto.CreateYapeChargeRequestDto;
import team.upao.dev.payments.culqi.dto.CreateYapeTokenRequestDto;
import team.upao.dev.payments.culqi.dto.CreateYapeTokenResponseDto;
import team.upao.dev.payments.culqi.dto.CulqiChargeRequest;
import team.upao.dev.payments.culqi.dto.CulqiChargeResponse;
import team.upao.dev.payments.culqi.dto.CulqiTokenResponse;
import team.upao.dev.payments.culqi.dto.CulqiYapeTokenRequest;
import team.upao.dev.payments.culqi.dto.YapeChargeResponseDto;
import team.upao.dev.payments.culqi.service.YapePaymentService;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class YapePaymentServiceImpl implements YapePaymentService {
    private static final long MIN_AMOUNT_IN_CENTS = 300; // S/ 3.00

    private final CulqiClient culqiClient;
    private final CulqiProperties culqiProperties;
    private final OrderService orderService;

    @Override
    @Transactional(readOnly = true)
    public CreateYapeTokenResponseDto createToken(CreateYapeTokenRequestDto requestDto) {
        OrderModel order = orderService.findModelById(requestDto.getOrderId());
        validateOrderIsPayable(order);

        long amountInCents = toAmountInCents(order.getTotalPrice());
        Map<String, String> metadata = new HashMap<>();
        metadata.put("order_id", order.getId().toString());
        if (order.getTable() != null && order.getTable().getId() != null) {
            metadata.put("table_id", order.getTable().getId().toString());
        }

        CulqiYapeTokenRequest culqiRequest = CulqiYapeTokenRequest.builder()
                .otp(requestDto.getOtp())
                .phoneNumber(requestDto.getPhoneNumber())
                .amount(String.valueOf(amountInCents))
                .metadata(metadata)
                .build();

        CulqiTokenResponse response = culqiClient.createYapeToken(culqiRequest);

        Instant expiresAt = response.getCreationDate() != null
                ? Instant.ofEpochMilli(response.getCreationDate()).plusSeconds(300)
                : null;

        return CreateYapeTokenResponseDto.builder()
                .tokenId(response.getId())
                .orderId(order.getId())
                .amount(amountInCents)
                .currency(culqiProperties.getDefaultCurrency())
                .expiresAt(expiresAt)
                .build();
    }

    @Override
    @Transactional
    public YapeChargeResponseDto createCharge(CreateYapeChargeRequestDto requestDto) {
        OrderModel order = orderService.findModelById(requestDto.getOrderId());
        validateOrderIsPayable(order);

        long amountInCents = toAmountInCents(order.getTotalPrice());

        CulqiChargeRequest chargeRequest = CulqiChargeRequest.builder()
                .amount(String.valueOf(amountInCents))
                .currencyCode(culqiProperties.getDefaultCurrency())
                .email(requestDto.getEmail())
                .sourceId(requestDto.getTokenId())
                .capture(true)
                .description(buildDescription(order))
                .metadata(buildMetadata(order, requestDto))
                .antifraudDetails(buildAntifraudDetails(requestDto))
                .build();

        CulqiChargeResponse chargeResponse = culqiClient.createCharge(chargeRequest);

        ChangeOrderStatusDto changeStatusDto = new ChangeOrderStatusDto();
        changeStatusDto.setOrderId(order.getId());
        changeStatusDto.setStatus(OrderStatus.PAID);
        orderService.changeStatus(changeStatusDto);

        return YapeChargeResponseDto.builder()
                .chargeId(chargeResponse.getId())
                .orderId(order.getId())
                .amount(chargeResponse.getAmount())
                .currency(chargeResponse.getCurrencyCode())
                .status(chargeResponse.getOutcome() != null ? chargeResponse.getOutcome().getType() : "unknown")
                .referenceCode(chargeResponse.getReferenceCode())
                .authorizationCode(chargeResponse.getAuthorizationCode())
                .userMessage(chargeResponse.getOutcome() != null ? chargeResponse.getOutcome().getUserMessage() : null)
                .operationDate(chargeResponse.getCreationDate() != null
                        ? Instant.ofEpochMilli(chargeResponse.getCreationDate())
                        : Instant.now())
                .build();
    }

    private void validateOrderIsPayable(OrderModel order) {
        if (order.getPaid() != null && order.getPaid()) {
            throw new IllegalArgumentException("El pedido ya fue pagado");
        }
        if (order.getOrderStatus() == OrderStatus.CANCELLED) {
            throw new IllegalArgumentException("No se puede cobrar un pedido cancelado");
        }
    }

    private long toAmountInCents(Double totalPrice) {
        double amount = totalPrice == null ? 0.0 : totalPrice;
        BigDecimal cents = BigDecimal.valueOf(amount)
                .multiply(BigDecimal.valueOf(100))
                .setScale(0, RoundingMode.HALF_UP);
        long value = cents.longValue();
        if (value < MIN_AMOUNT_IN_CENTS) {
            throw new IllegalArgumentException("El monto mínimo para cobrar con Yape es de S/ 3.00");
        }
        return value;
    }

    private String buildDescription(OrderModel order) {
        String tableLabel = order.getTable() != null ? order.getTable().getNumber() : "sin mesa";
        return String.format(Locale.ROOT, "Consumo mesa %s - Pedido #%d", tableLabel, order.getId());
    }

    private Map<String, String> buildMetadata(OrderModel order, CreateYapeChargeRequestDto requestDto) {
        Map<String, String> metadata = new HashMap<>();
        metadata.put("order_id", order.getId().toString());
        metadata.put("document_number", requestDto.getDocumentNumber());
        metadata.put("document_type", requestDto.getDocumentType());
        if (order.getTable() != null && order.getTable().getId() != null) {
            metadata.put("table_id", order.getTable().getId().toString());
        }
        return metadata;
    }

    private CulqiChargeRequest.AntifraudDetails buildAntifraudDetails(CreateYapeChargeRequestDto requestDto) {
        String[] names = splitFullName(requestDto.getFullName());
        return CulqiChargeRequest.AntifraudDetails.builder()
                .firstName(names[0])
                .lastName(names[1])
                .address(requestDto.getAddress())
                .addressCity(requestDto.getCity())
                .countryCode(requestDto.getCountryCode())
                .phoneNumber(requestDto.getPhoneNumber())
                .build();
    }

    private String[] splitFullName(String fullName) {
        if (fullName == null || fullName.isBlank()) {
            return new String[]{"Cliente", "Yape"};
        }
        String trimmed = fullName.trim();
        int idx = trimmed.lastIndexOf(' ');
        if (idx <= 0) {
            return new String[]{trimmed, trimmed};
        }
        String firstName = trimmed.substring(0, idx).trim();
        String lastName = trimmed.substring(idx + 1).trim();
        if (firstName.isEmpty()) {
            firstName = lastName;
        }
        if (lastName.isEmpty()) {
            lastName = firstName;
        }
        return new String[]{firstName, lastName};
    }
}
