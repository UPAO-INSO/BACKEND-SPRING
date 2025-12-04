package team.upao.dev.payments.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import team.upao.dev.payments.enums.PaymentType;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PaymentRequestDto {
    @NotBlank(message = "Provider is required")
    private String provider;

    private String externalId;

    @NotNull(message = "Amount is is not will be null")
    private Integer amount;

    @NotBlank(message = "Currency code is required")
    private String currencyCode;

    @NotBlank(message = "Description is required")
    private String description;

    @NotNull(message = "Order ID is required")
    private UUID orderId;

    @NotNull(message = "Customer ID is required")
    private Long customerId;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "Payment type is required")
    private PaymentType paymentType;

    @NotBlank(message = "State is required")
    private String state;

    private BigDecimal totalFee;

    private BigDecimal netAmount;

    private String qr;

    private String urlPe;

    @NotNull(message = "Creation date is required")
    private Instant creationDate;

    @NotNull(message = "Expiration date is required")
    private Instant expirationDate;

    @NotNull(message = "Updated at is required")
    private Instant updatedAt;

    private Instant paidAt;

    private String rawResponse;

    @Data
    public static class Metadata {
        @JsonProperty("customer_id")
        Long customerId;
    }
}
