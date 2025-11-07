package team.upao.dev.orders.service;

import org.springframework.lang.NonNull;
import team.upao.dev.common.dto.PaginationRequestDto;
import team.upao.dev.common.dto.PaginationResponseDto;
import team.upao.dev.orders.dto.ChangeOrderStatusDto;
import team.upao.dev.orders.dto.OrderRequestDto;
import team.upao.dev.orders.dto.OrderResponseDto;
import team.upao.dev.orders.dto.ServeProductOrderRequestDto;
import team.upao.dev.orders.enums.OrderStatus;
import team.upao.dev.orders.model.OrderModel;

import java.util.List;

public interface OrderService {
    OrderResponseDto create(OrderRequestDto order);
    OrderResponseDto serveProductOrder(ServeProductOrderRequestDto request);
    PaginationResponseDto<OrderResponseDto> findAll(PaginationRequestDto requestDto, OrderStatus status);
    PaginationResponseDto<OrderResponseDto> findAllByArrayStatus(PaginationRequestDto requestDto, List<OrderStatus> status);
    PaginationResponseDto<OrderResponseDto> findAllByTablesAndStatus(PaginationRequestDto requestDto, List<Long> tableIds, List<OrderStatus> status);
    OrderResponseDto findById(Long id);
    List<OrderResponseDto> findByTableIds(List<Long> tableId);
    PaginationResponseDto<OrderResponseDto> findAllByTableId(PaginationRequestDto requestDto, Long tableId);
    OrderModel findModelById(Long id);
    OrderResponseDto update(Long id, OrderRequestDto order);
    OrderResponseDto changeStatus(@NonNull ChangeOrderStatusDto changeOrderStatusDto);
    String delete(Long id);
}
