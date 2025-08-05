package upao.inso.dclassic.orders.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import upao.inso.dclassic.orders.dto.OrderDto;
import upao.inso.dclassic.orders.dto.OrderEmployeeDto;
import upao.inso.dclassic.orders.model.OrderModel;
import upao.inso.dclassic.products.dto.ProductOrderRequestDto;
import upao.inso.dclassic.products.dto.ProductOrderResponseDto;
import upao.inso.dclassic.products.mapper.ProductOrderMapper;

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
