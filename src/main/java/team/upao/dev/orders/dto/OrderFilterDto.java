package team.upao.dev.orders.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import team.upao.dev.orders.enums.OrderStatus;

import java.util.List;

@Getter @Setter
@Builder
@AllArgsConstructor @NoArgsConstructor
public class OrderFilterDto {
    @NotNull(message = "tableIds are required")
    private List<Long> tableIds;
    @NotNull(message = "orderStatus are required")
    private List<OrderStatus> orderStatus;
}
