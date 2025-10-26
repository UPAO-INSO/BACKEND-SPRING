package team.upao.dev.orders.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import team.upao.dev.employees.model.EmployeeModel;

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
    @JsonBackReference("order-employees")
    private OrderModel order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id", nullable = false)
    private EmployeeModel employee;
}
