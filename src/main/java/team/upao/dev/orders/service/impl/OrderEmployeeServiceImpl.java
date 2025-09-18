package team.upao.dev.orders.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import team.upao.dev.common.dto.PaginationRequestDto;
import team.upao.dev.common.dto.PaginationResponseDto;
import team.upao.dev.common.utils.PaginationUtils;
import team.upao.dev.orders.dto.OrderEmployeeDto;
import team.upao.dev.orders.mapper.OrderEmployeeMapper;
import team.upao.dev.orders.model.OrderEmployeeModel;
import team.upao.dev.orders.repository.IOrderEmployeeRepository;
import team.upao.dev.orders.service.OrderEmployeeService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderEmployeeServiceImpl implements OrderEmployeeService {
    private final IOrderEmployeeRepository orderEmployeeRepository;
    private final OrderEmployeeMapper orderEmployeeMapper;

    @Override
    public OrderEmployeeDto create(OrderEmployeeModel orderEmployee) {
        return orderEmployeeMapper.toDto(orderEmployeeRepository.save(orderEmployee));
    }

    @Override
    public PaginationResponseDto<OrderEmployeeDto> findAll(PaginationRequestDto requestDto) {
        final Pageable pageable = PaginationUtils.getPageable(requestDto);
        final Page<OrderEmployeeModel> entities = this.orderEmployeeRepository.findAll(pageable);
        final List<OrderEmployeeModel> orderEmployeeModels = entities.getContent();
        List<Long> ordersId =  orderEmployeeModels.stream()
                .map(orderEmployeeModel -> orderEmployeeModel.getOrder().getId())
                .toList();

        final List<OrderEmployeeDto> orderEmployeeDtos = orderEmployeeMapper.toDto(entities.getContent());
        return new PaginationResponseDto<>(
                orderEmployeeDtos,
                entities.getTotalPages(),
                entities.getTotalElements(),
                entities.getSize(),
                entities.getNumber()+1,
                entities.isEmpty()
        );
    }

    @Override
    public OrderEmployeeDto findById(Long id) {
        return null;
    }

    @Override
    public OrderEmployeeDto update(Long id, OrderEmployeeDto orderEmployee) {
        return null;
    }

    @Override
    public void delete(Long id) {

    }
}
