package upao.inso.dclassic.orders.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import upao.inso.dclassic.common.dto.PaginationRequestDto;
import upao.inso.dclassic.common.dto.PaginationResponseDto;
import upao.inso.dclassic.common.utils.PaginationUtils;
import upao.inso.dclassic.orders.dto.OrderEmployeeDto;
import upao.inso.dclassic.orders.mapper.OrderEmployeeMapper;
import upao.inso.dclassic.orders.model.OrderEmployeeModel;
import upao.inso.dclassic.orders.repository.IOrderEmployeeRepository;
import upao.inso.dclassic.orders.service.OrderEmployeeService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderEmployeeServiceImpl implements OrderEmployeeService {
    private final IOrderEmployeeRepository orderEmployeeRepository;
    private final OrderEmployeeMapper orderEmployeeMapper;

    @Override
    public OrderEmployeeModel create(OrderEmployeeDto orderEmployeeDto) {
        OrderEmployeeModel model = orderEmployeeMapper.toEntity(orderEmployeeDto);
        return orderEmployeeRepository.save(model);
    }

    @Override
    public PaginationResponseDto<OrderEmployeeModel> findAll(PaginationRequestDto requestDto) {
        final Pageable pageable = PaginationUtils.getPageable(requestDto);
        final Page<OrderEmployeeModel> entities = this.orderEmployeeRepository.findAll(pageable);
        final List<OrderEmployeeModel> entitiesDto = entities.stream().toList();
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
    public OrderEmployeeModel findById(Long id) {
        return null;
    }

    @Override
    public OrderEmployeeModel update(Long id, OrderEmployeeModel orderEmployee) {
        return null;
    }

    @Override
    public void delete(Long id) {

    }
}
