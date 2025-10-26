package team.upao.dev.orders.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import team.upao.dev.orders.enums.OrderStatus;
import team.upao.dev.products.dto.ProductOrderRequestDto;

import java.time.Instant;
import java.util.List;

@Data
@Builder
public class OrderRequestDto {
    private OrderStatus orderStatus;

    private String comment;

    @NotNull
    private Boolean paid;

    @NotNull
    private Long tableId;

    @NotNull
    private Integer totalItems;

    @NotNull
    private Double totalPrice;

    private Instant createdAt;

    private Instant paidAt;

    @NotNull
    private List<ProductOrderRequestDto> productOrders;

    @NotNull
    private List<OrderEmployeeDto> orderEmployees;
}
