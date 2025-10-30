package team.upao.dev.orders.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;
import team.upao.dev.employees.model.EmployeeModel;

@Getter @Setter
@AllArgsConstructor @NoArgsConstructor
@Builder
@Entity
@Table(
        name = "order_employee",
        indexes = {
                @Index(name = "idx_order_employee_order_id", columnList = "order_id"),
                @Index(name = "idx_order_employee_employee_id", columnList = "employee_id")
        }
)
public class OrderEmployeeModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer minutesSpent;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    @JsonBackReference("order-employees")
    private OrderModel order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id", nullable = false)
    private EmployeeModel employee;
}
