package team.upao.dev.orders.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import team.upao.dev.orders.enums.OrderStatus;

import java.util.List;

@Data
@Builder
@AllArgsConstructor @NoArgsConstructor
public class OrderFilterDto {
    @NotNull(message = "tableIds are required")
    private List<Long> tableIds;
    @NotNull(message = "orderStatus are required")
    private List<OrderStatus> orderStatus;
}
