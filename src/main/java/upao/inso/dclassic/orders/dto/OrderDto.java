package upao.inso.dclassic.orders.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import upao.inso.dclassic.orders.enums.OrderStatus;
import upao.inso.dclassic.products.dto.ProductOrderRequestDto;
import upao.inso.dclassic.products.dto.ProductOrderResponseDto;

import java.time.Instant;
import java.util.List;

@Data
@Builder
public class OrderDto {
    private Long id;

    private OrderStatus orderStatus;

    @NotBlank
    private String comment;

    private Boolean paid;

    private Long tableId;

    private Long clientId;

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
