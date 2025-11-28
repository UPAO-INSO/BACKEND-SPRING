package team.upao.dev.orders.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.UUID;

@Getter @Setter
@Builder
@NoArgsConstructor @AllArgsConstructor
public class ServeProductOrderRequestDto {
    @NotNull(message = "Order ID cannot be null")
    UUID orderId;

    @NotNull(message = "Product Order ID cannot be null")
    Long productOrderId;

    @NotNull(message = "Quantity cannot be null")
    Integer quantity;
}
