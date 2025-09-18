package team.upao.dev.orders.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import team.upao.dev.orders.dto.OrderDto;
import team.upao.dev.orders.dto.OrderEmployeeDto;
import team.upao.dev.orders.model.OrderModel;
import team.upao.dev.products.dto.ProductOrderResponseDto;
import team.upao.dev.products.mapper.ProductOrderMapper;

import java.util.List;

@Component
@RequiredArgsConstructor
public class OrderMapper {
    private final ProductOrderMapper productOrderMapper;
    private final OrderEmployeeMapper orderEmployeeMapper;

    public OrderModel toModel(OrderDto orderDto) {
        return OrderModel.builder()
                .id(orderDto.getId())
                .comment(orderDto.getComment())
                .paid(orderDto.getPaid())
                .table(null)
                .client(null)
                .productOrders(null)
                .ordersEmployee(null)
                .build();
    }

    public OrderDto toDto(OrderModel order) {
        List<ProductOrderResponseDto> productOrders = productOrderMapper.toDto(order.getProductOrders());
        List<OrderEmployeeDto> orderEmployees = orderEmployeeMapper.toDto(order.getOrdersEmployee());

        return OrderDto.builder()
                .id(order.getId())
                .orderStatus(order.getOrderStatus())
                .comment(order.getComment())
                .paid(order.getPaid())
                .totalItems(order.getTotalItems())
                .totalPrice(order.getTotalPrice())
                .tableId(order.getTable().getId())
                .clientId(order.getClient().getId())
                .createdAt(order.getCreatedAt())
                .updatedAt(order.getUpdatedAt())
                .paidAt(order.getPaidAt())
                .productOrders(productOrders)
                .orderEmployees(orderEmployees)
                .build();
    }

    public List<OrderDto> toDto(List<OrderModel> orders) {
        return orders.stream()
                .map(this::toDto)
                .toList();
    }
}
