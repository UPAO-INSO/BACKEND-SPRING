package team.upao.dev.orders.dto;

import lombok.*;

import java.util.UUID;

@Getter @Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderEmployeeResponseDto {
    private Long id;
    private UUID orderId;
    private Long employeeId;
    private Integer minutesSpent;
    private String employeeName;
    private String employeeLastname;
}
