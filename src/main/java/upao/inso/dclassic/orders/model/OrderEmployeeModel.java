package upao.inso.dclassic.orders.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import upao.inso.dclassic.employees.model.EmployeeModel;

import java.time.Instant;

@Data @AllArgsConstructor @NoArgsConstructor
@Builder
@Entity
@Table(name = "order_employee")
public class OrderEmployeeModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer minutesSpent;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private OrderModel order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id", nullable = false)
    private EmployeeModel employee;
}
