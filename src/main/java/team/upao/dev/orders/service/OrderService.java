package team.upao.dev.orders.service;

import org.springframework.lang.NonNull;
import team.upao.dev.common.dto.PaginationRequestDto;
import team.upao.dev.common.dto.PaginationResponseDto;
import team.upao.dev.orders.dto.ChangeOrderStatusDto;
import team.upao.dev.orders.dto.OrderDto;
import team.upao.dev.orders.enums.OrderStatus;
import team.upao.dev.orders.model.OrderModel;

import java.util.List;

public interface OrderService {
    OrderDto create(OrderDto order);
    PaginationResponseDto<OrderDto> findAll(PaginationRequestDto requestDto);
    PaginationResponseDto<OrderDto> findAllByArrayStatus(PaginationRequestDto requestDto, List<OrderStatus> status);
    List<OrderDto> findAllByOrderStatus(OrderStatus status);
    OrderDto findById(Long id);
    List<OrderDto> findByTableIds(List<Long> tableId);
    OrderModel findModelById(Long id);
    OrderDto update(Long id, OrderDto order);
    OrderDto changeStatus(@NonNull ChangeOrderStatusDto changeOrderStatusDto);
    String delete(Long id);
}
