package upao.inso.dclassic.orders.dto;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import upao.inso.dclassic.common.decorators.ValidEnum;
import upao.inso.dclassic.orders.enums.OrderStatus;

@Data
public class ChangeOrderStatusDto {
    @NotNull
    private Long orderId;

    @NotNull
    @ValidEnum(enumClass = OrderStatus.class, message = "Invalid status, must be one of: {values}")
    @Enumerated(EnumType.STRING)
    private OrderStatus status;
}
