package team.upao.dev.orders.dto;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import team.upao.dev.common.decorators.ValidEnum;
import team.upao.dev.orders.enums.OrderStatus;

import java.util.UUID;

@Getter @Setter
public class ChangeOrderStatusDto {
    @NotNull
    private UUID orderId;

    @NotNull
    @ValidEnum(enumClass = OrderStatus.class, message = "Invalid status, must be one of: {values}")
    @Enumerated(EnumType.STRING)
    private OrderStatus status;
}
