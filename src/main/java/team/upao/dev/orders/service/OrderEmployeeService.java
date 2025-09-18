package team.upao.dev.orders.service;

import team.upao.dev.common.dto.PaginationRequestDto;
import team.upao.dev.common.dto.PaginationResponseDto;
import team.upao.dev.orders.dto.OrderEmployeeDto;
import team.upao.dev.orders.model.OrderEmployeeModel;

public interface OrderEmployeeService {
    OrderEmployeeDto create(OrderEmployeeModel orderEmployee);
    PaginationResponseDto<OrderEmployeeDto> findAll(PaginationRequestDto requestDto);
    OrderEmployeeDto findById(Long id);
    OrderEmployeeDto update(Long id, OrderEmployeeDto orderEmployee);
    void delete(Long id);
}
