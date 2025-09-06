package upao.inso.dclassic.orders.service;

import org.springframework.lang.NonNull;
import upao.inso.dclassic.common.dto.PaginationRequestDto;
import upao.inso.dclassic.common.dto.PaginationResponseDto;
import upao.inso.dclassic.orders.dto.ChangeOrderStatusDto;
import upao.inso.dclassic.orders.dto.OrderDto;
import upao.inso.dclassic.orders.enums.OrderStatus;
import upao.inso.dclassic.orders.model.OrderModel;

import java.util.List;

public interface OrderService {
    OrderDto create(OrderDto order);
    PaginationResponseDto<OrderDto> findAll(PaginationRequestDto requestDto);
    List<OrderDto> findAllByOrderStatus(OrderStatus status);
    OrderDto findById(Long id);
    OrderModel findModelById(Long id);
    OrderDto update(Long id, OrderDto order);
    OrderDto changeStatus(@NonNull ChangeOrderStatusDto changeOrderStatusDto);
    String delete(Long id);
}
