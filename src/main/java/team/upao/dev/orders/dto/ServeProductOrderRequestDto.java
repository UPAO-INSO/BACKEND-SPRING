package team.upao.dev.orders.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ServeProductOrderRequestDto {
    @NotNull
    Long orderId;

    @NotNull
    Long productOrderId;

    @NotNull
    @Min(1)
    Integer quantity;
}
