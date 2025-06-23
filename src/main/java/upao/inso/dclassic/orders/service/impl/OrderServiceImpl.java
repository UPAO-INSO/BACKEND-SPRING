package upao.inso.dclassic.orders.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import upao.inso.dclassic.common.dto.PaginationRequestDto;
import upao.inso.dclassic.common.dto.PaginationResponseDto;
import upao.inso.dclassic.common.utils.PaginationUtils;
import upao.inso.dclassic.orders.dto.OrderEmployeeDto;
import upao.inso.dclassic.orders.model.OrderModel;
import upao.inso.dclassic.orders.repository.IOrderRepository;
import upao.inso.dclassic.orders.service.OrderEmployeeService;
import upao.inso.dclassic.orders.service.OrderService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final IOrderRepository orderRepository;
    private final OrderEmployeeService orderEmployeeService;

    @Override
    public OrderModel create(Long employeeId, OrderModel order) {
        OrderModel newOrder = orderRepository.save(order);

        orderEmployeeService.create(new OrderEmployeeDto(employeeId, newOrder.getId()));

        return newOrder;
    }

    @Override
    public PaginationResponseDto<OrderModel> findAll(PaginationRequestDto requestDto) {
        final Pageable pageable = PaginationUtils.getPageable(requestDto);
        final Page<OrderModel> entities = orderRepository.findAll(pageable);
        final List<OrderModel> entitiesDto = entities.stream().toList();
        return new PaginationResponseDto<>(
                entitiesDto,
                entities.getTotalPages(),
                entities.getTotalElements(),
                entities.getSize(),
                entities.getNumber()+1,
                entities.isEmpty()
        );
    }

    @Override
    public OrderModel findById(Long id) {
        return null;
    }

    @Override
    public OrderModel update(Long id, OrderModel order) {
        return null;
    }

    @Override
    public void delete(Long id) {
        String msg = "orderRepository.deleteById(id)";
    }
}
