package team.upao.dev.payments.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import team.upao.dev.customers.dto.CustomerResponseDto;
import team.upao.dev.orders.dto.OrderResponseDto;
import team.upao.dev.payments.enums.PaymentType;

import java.math.BigDecimal;
import java.time.Instant;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PaymentResponseDto {

    @NotNull(message = "Id cannot be null")
    private Long id;

    @NotBlank(message = "Provider cannot be blank")
    private String provider;

    @NotBlank(message = "External ID cannot be blank")
    private String externalId;

    @NotNull(message = "Amount cannot be null")
    private Integer amount;

    @NotBlank(message = "Currency code cannot be blank")
    private String currencyCode;

    @NotBlank(message = "Description cannot be blank")
    private String description;

    @NotNull(message = "Order cannot be null")
    private OrderResponseDto order;

    @NotNull(message = "Customer cannot be null")
    private CustomerResponseDto customer;

    private PaymentType paymentType;

    @NotBlank(message = "State cannot be blank")
    private String state;

    @NotNull(message = "Total fee cannot be null")
    private BigDecimal totalFee;

    @NotNull(message = "Net amount cannot be null")
    private BigDecimal netAmount;

    @NotBlank(message = "QR cannot be blank")
    private String qr;

    @NotBlank(message = "URL PE cannot be blank")
    private String urlPe;

    @NotNull(message = "Creation date cannot be null")
    private Instant creationDate;

    @NotNull(message = "Expiration date cannot be null")
    private Instant expirationDate;

    @NotNull(message = "Updated at cannot be null")
    private Instant updatedAt;

    @NotNull(message = "PaitAt at cannot be null")
    private Instant paidAt;

    @NotBlank(message = "Raw response cannot be blank")
    private String rawResponse;

    private Instant createdAt;

    private Instant modifiedAt;
}
