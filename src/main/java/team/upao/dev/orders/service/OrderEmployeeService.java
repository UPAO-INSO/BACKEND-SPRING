package team.upao.dev.orders.service;

import team.upao.dev.common.dto.PaginationRequestDto;
import team.upao.dev.common.dto.PaginationResponseDto;
import team.upao.dev.orders.dto.OrderEmployeeResponseDto;
import team.upao.dev.orders.model.OrderEmployeeModel;

public interface OrderEmployeeService {
    OrderEmployeeResponseDto create(OrderEmployeeModel orderEmployee);
    PaginationResponseDto<OrderEmployeeResponseDto> findAll(PaginationRequestDto requestDto);
    OrderEmployeeResponseDto findById(Long id);
    OrderEmployeeResponseDto update(Long id, OrderEmployeeResponseDto orderEmployee);
    void delete(Long id);
}
