package team.upao.dev.orders.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OrderEmployeeDto {
    private Long id;
    private Long orderId;
    private Long employeeId;
    private Integer minutesSpent;
    private String employeeName;
    private String employeeLastname;
}
