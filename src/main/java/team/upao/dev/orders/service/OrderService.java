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
import java.util.UUID;

public interface OrderService {
    OrderResponseDto create(OrderRequestDto order);
    OrderResponseDto serveProductOrder(ServeProductOrderRequestDto request);
    PaginationResponseDto<OrderResponseDto> findAll(PaginationRequestDto requestDto, OrderStatus status);
    PaginationResponseDto<OrderResponseDto> findAllByArrayStatus(PaginationRequestDto requestDto, List<OrderStatus> status);
    PaginationResponseDto<OrderResponseDto> findAllByTablesAndStatus(PaginationRequestDto requestDto, List<Long> tableIds, List<OrderStatus> status);
    OrderResponseDto findById(UUID id);
    List<OrderResponseDto> findByTableIds(List<Long> tableId);
    PaginationResponseDto<OrderResponseDto> findAllByTableId(PaginationRequestDto requestDto, Long tableId);
    OrderModel findModelById(UUID id);
    OrderResponseDto update(UUID id, OrderRequestDto order);
    OrderResponseDto changeStatus(@NonNull ChangeOrderStatusDto changeOrderStatusDto);
    String delete(UUID id);
}
