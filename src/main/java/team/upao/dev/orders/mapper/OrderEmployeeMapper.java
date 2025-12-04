package team.upao.dev.orders.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import team.upao.dev.employees.model.EmployeeModel;
import team.upao.dev.orders.dto.OrderEmployeeResponseDto;
import team.upao.dev.orders.model.OrderEmployeeModel;
import team.upao.dev.orders.model.OrderModel;

import java.util.List;

@Component
@RequiredArgsConstructor
public class OrderEmployeeMapper {
    public OrderEmployeeModel toModel(OrderEmployeeResponseDto dto, OrderModel order, EmployeeModel employee) {
        return OrderEmployeeModel.builder()
                .minutesSpent(dto.getMinutesSpent() != null ? dto.getMinutesSpent() : 0)
                .order(order)
                .employee(employee)
                .build();
    }

    public OrderEmployeeResponseDto toDto(OrderEmployeeModel orderEmployee) {
        return OrderEmployeeResponseDto.builder()
                .id(orderEmployee.getId())
                .orderId(orderEmployee.getOrder().getId())
                .employeeId(orderEmployee.getEmployee().getId())
                .minutesSpent(orderEmployee.getMinutesSpent())
                .employeeName(orderEmployee.getEmployee().getName())
                .employeeLastname(orderEmployee.getEmployee().getLastname())
                .build();
    }

    public List<OrderEmployeeResponseDto> toDto(List<OrderEmployeeModel> ordersEmployee) {
        return ordersEmployee.stream()
                .map(this::toDto)
                .toList();
    }
}
