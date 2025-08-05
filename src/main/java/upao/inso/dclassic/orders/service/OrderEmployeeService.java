package upao.inso.dclassic.orders.service;

import upao.inso.dclassic.common.dto.PaginationRequestDto;
import upao.inso.dclassic.common.dto.PaginationResponseDto;
import upao.inso.dclassic.orders.dto.OrderEmployeeDto;
import upao.inso.dclassic.orders.model.OrderEmployeeModel;

public interface OrderEmployeeService {
    OrderEmployeeDto create(OrderEmployeeModel orderEmployee);
    PaginationResponseDto<OrderEmployeeDto> findAll(PaginationRequestDto requestDto);
    OrderEmployeeDto findById(Long id);
    OrderEmployeeDto update(Long id, OrderEmployeeDto orderEmployee);
    void delete(Long id);
}
