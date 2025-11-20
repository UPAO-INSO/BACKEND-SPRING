package team.upao.dev.orders.dto;

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

    /**
     * Cantidad a incrementar o decrementar en el servido.
     * Valores positivos: marcar como servido (ej: 1, 2, 3)
     * Valores negativos: desmarcar servido (ej: -1, -2, -3)
     */
    @NotNull(message = "Quantity cannot be null")
    Integer quantity;
}
