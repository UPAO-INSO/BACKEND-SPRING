package upao.inso.dclassic.orders.service;

import upao.inso.dclassic.common.dto.PaginationRequestDto;
import upao.inso.dclassic.common.dto.PaginationResponseDto;
import upao.inso.dclassic.orders.dto.OrderEmployeeDto;
import upao.inso.dclassic.orders.model.OrderEmployeeModel;

public interface OrderEmployeeService {
    OrderEmployeeModel create(OrderEmployeeDto orderEmployee);
    PaginationResponseDto<OrderEmployeeModel> findAll(PaginationRequestDto requestDto);
    OrderEmployeeModel findById(Long id);
    OrderEmployeeModel update(Long id, OrderEmployeeModel orderEmployee);
    void delete(Long id);
}
