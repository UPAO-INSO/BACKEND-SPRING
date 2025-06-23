package upao.inso.dclassic.orders.service;

import upao.inso.dclassic.common.dto.PaginationRequestDto;
import upao.inso.dclassic.common.dto.PaginationResponseDto;
import upao.inso.dclassic.orders.model.OrderModel;

public interface OrderService {
    OrderModel create(Long employeeId, OrderModel order);
    PaginationResponseDto<OrderModel> findAll(PaginationRequestDto requestDto);
    OrderModel findById(Long id);
    OrderModel update(Long id, OrderModel order);
    void delete(Long id);
}
