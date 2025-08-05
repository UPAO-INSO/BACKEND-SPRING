package upao.inso.dclassic.orders.mapper;

import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import upao.inso.dclassic.employees.model.EmployeeModel;
import upao.inso.dclassic.orders.dto.OrderEmployeeDto;
import upao.inso.dclassic.orders.model.OrderEmployeeModel;
import upao.inso.dclassic.orders.model.OrderModel;

import java.util.List;

@Component
@RequiredArgsConstructor
public class OrderEmployeeMapper {
    public OrderEmployeeModel toModel(OrderEmployeeDto dto, OrderModel order, EmployeeModel employee) {
        return OrderEmployeeModel.builder()
                .minutesSpent(dto.getMinutesSpent() != null ? dto.getMinutesSpent() : 0)
                .order(order)
                .employee(employee)
                .build();
    }

    public OrderEmployeeDto toDto(OrderEmployeeModel orderEmployee) {
        return OrderEmployeeDto.builder()
                .id(orderEmployee.getId())
                .orderId(orderEmployee.getOrder().getId())
                .employeeId(orderEmployee.getEmployee().getId())
                .minutesSpent(orderEmployee.getMinutesSpent())
                .employeeName(orderEmployee.getEmployee().getName())
                .employeeLastname(orderEmployee.getEmployee().getLastname())
                .build();
    }

    public List<OrderEmployeeDto> toDto(List<OrderEmployeeModel> ordersEmployee) {
        return ordersEmployee.stream()
                .map(this::toDto)
                .toList();
    }
}
