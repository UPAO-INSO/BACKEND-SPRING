package upao.inso.dclassic.orders.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor @NoArgsConstructor
public class OrderEmployeeDto {
    private Long orderId;
    private Long employeeId;
}
