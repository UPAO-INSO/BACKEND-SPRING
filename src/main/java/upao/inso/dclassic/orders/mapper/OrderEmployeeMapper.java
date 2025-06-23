package upao.inso.dclassic.orders.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import upao.inso.dclassic.employees.model.EmployeeModel;
import upao.inso.dclassic.employees.repository.IEmployeeRepository;
import upao.inso.dclassic.orders.dto.OrderEmployeeDto;
import upao.inso.dclassic.orders.model.OrderEmployeeModel;
import upao.inso.dclassic.orders.model.OrderModel;
import upao.inso.dclassic.orders.repository.IOrderRepository;

@Component
@RequiredArgsConstructor
public class OrderEmployeeMapper {
    private final IOrderRepository orderRepository;
    private final IEmployeeRepository employeeRepository;

    public OrderEmployeeModel toEntity(OrderEmployeeDto dto) {
        OrderModel order = orderRepository.findById(dto.getOrderId())
                .orElseThrow(() -> new IllegalArgumentException("Order not found with ID " + dto.getOrderId()));
        EmployeeModel employee = employeeRepository.findById(dto.getEmployeeId())
                .orElseThrow(() -> new IllegalArgumentException("Employee not found with ID " + dto.getEmployeeId()));

        return OrderEmployeeModel.builder()
                .order(order)
                .employee(employee)
                .build();
    }
}
