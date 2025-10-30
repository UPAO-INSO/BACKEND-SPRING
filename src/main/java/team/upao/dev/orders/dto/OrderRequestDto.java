package team.upao.dev.orders.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import team.upao.dev.orders.enums.OrderStatus;
import team.upao.dev.products.dto.ProductOrderRequestDto;

import java.util.List;

@Getter @Setter
@Builder
public class OrderRequestDto {
    private OrderStatus orderStatus;

    private String comment;

    @NotNull
    private Long tableId;

    private Boolean paid;

    @NotNull
    private List<ProductOrderRequestDto> productOrders;

    @NotNull
    private List<OrderEmployeeDto> orderEmployees;
}
