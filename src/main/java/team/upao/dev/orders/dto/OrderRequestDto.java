package team.upao.dev.orders.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import team.upao.dev.orders.enums.OrderStatus;
import team.upao.dev.products.dto.ProductOrderRequestDto;

import java.util.List;

@Getter @Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderRequestDto {
    private OrderStatus orderStatus;

    private String comment;

    @NotNull
    private Long tableId;

    @NotNull
    private List<ProductOrderRequestDto> productOrders;

    @NotNull
    private List<OrderEmployeeRequestDto> orderEmployees;
}
