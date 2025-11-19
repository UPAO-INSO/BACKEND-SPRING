package team.upao.dev.orders.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter @Setter
@Builder
@NoArgsConstructor @AllArgsConstructor
public class ServeProductOrderRequestDto {
    @NotNull(message = "Order ID cannot be null")
    Long orderId;

    @NotNull(message = "Product Order ID cannot be null")
    Long productOrderId;

    @NotNull(message = "Quantity cannot be null")
    @Min(1)
    Integer quantity;
}
