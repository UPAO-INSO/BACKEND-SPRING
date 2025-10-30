package team.upao.dev.orders.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import team.upao.dev.orders.enums.OrderStatus;
import team.upao.dev.products.dto.ProductOrderResponseDto;

import java.time.Instant;
import java.util.List;

@Getter @Setter
@Builder
public class OrderResponseDto {
    private Long id;

    private OrderStatus orderStatus;

    private String comment;

    private Boolean paid;

    @NotNull
    private Long tableId;

    private Integer totalItems;

    private Double totalPrice;

    private Instant createdAt;
    private Instant updatedAt;
    private Instant paidAt;

    @NotNull
    private List<ProductOrderResponseDto> productOrders;

    @NotNull
    private List<OrderEmployeeDto> orderEmployees;
}
