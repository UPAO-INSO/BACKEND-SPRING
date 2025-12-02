package team.upao.dev.orders.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import team.upao.dev.orders.dto.OrderRequestDto;
import team.upao.dev.orders.dto.OrderResponseDto;
import team.upao.dev.orders.dto.OrderEmployeeResponseDto;
import team.upao.dev.orders.model.OrderModel;
import team.upao.dev.products.dto.ProductOrderResponseDto;
import team.upao.dev.products.mapper.ProductOrderMapper;

import java.util.List;

@Component
@RequiredArgsConstructor
public class OrderMapper {
    private final ProductOrderMapper productOrderMapper;
    private final OrderEmployeeMapper orderEmployeeMapper;

    public OrderModel toModel(OrderRequestDto orderResponseDto) {
        return OrderModel.builder()
                .comment(orderResponseDto.getComment())
                .paid(false)
                .table(null)
                .productOrders(null)
                .ordersEmployee(null)
                .build();
    }

    public OrderResponseDto toDto(OrderModel order) {
        List<ProductOrderResponseDto> productOrders = productOrderMapper.toDto(order.getProductOrders());
        List<OrderEmployeeResponseDto> orderEmployees = orderEmployeeMapper.toDto(order.getOrdersEmployee());

        return OrderResponseDto.builder()
                .id(order.getId())
                .orderStatus(order.getOrderStatus())
                .comment(order.getComment())
                .paid(order.getPaid())
                .totalItems(order.getTotalItems())
                .totalPrice(order.getTotalPrice())
                .tableId(order.getTable().getId())
                .createdAt(order.getCreatedAt())
                .updatedAt(order.getUpdatedAt())
                .paidAt(order.getPaidAt())
                .productOrders(productOrders)
                .orderEmployees(orderEmployees)
                .build();
    }

    public List<OrderResponseDto> toDto(List<OrderModel> orders) {
        return orders.stream()
                .map(this::toDto)
                .toList();
    }
}
