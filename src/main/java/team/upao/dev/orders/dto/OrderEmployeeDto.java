package team.upao.dev.orders.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter @Setter
@Builder
public class OrderEmployeeDto {
    private Long id;
    private UUID orderId;
    private Long employeeId;
    private Integer minutesSpent;
    private String employeeName;
    private String employeeLastname;
}
