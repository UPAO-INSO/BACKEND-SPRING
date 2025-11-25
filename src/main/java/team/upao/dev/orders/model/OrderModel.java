package team.upao.dev.orders.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import team.upao.dev.orders.enums.OrderStatus;
import team.upao.dev.products.model.ProductOrderModel;
import team.upao.dev.tables.model.TableModel;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@EntityListeners(AuditingEntityListener.class)
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
@Entity
@Table(
        name = "orders",
        indexes = {
                @Index(name = "idx_orders_order_status", columnList = "orderStatus"),
                @Index(name = "idx_orders_table_id", columnList = "table_id")
        }
)
public class OrderModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private  Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private OrderStatus orderStatus = OrderStatus.PENDING;

    @Column(nullable = false)
    private String comment;

    @Builder.Default
    @Column(nullable = false, columnDefinition = "BOOLEAN DEFAULT FALSE")
    private Boolean paid = false;

    @Column(nullable = false)
    private Integer totalItems;

    @Column(nullable = false)
    private Double totalPrice;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "table_id")
    private TableModel table;

    @Builder.Default
    @OneToMany(mappedBy = "order", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference("order-productOrders")
    private List<ProductOrderModel> productOrders = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "order", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonManagedReference("order-employees")
    private List<OrderEmployeeModel> ordersEmployee = new ArrayList<>();

    @Column(nullable = false)
    @CreatedDate
    private Instant createdAt;

    @Column(nullable = false)
    @LastModifiedDate
    private Instant updatedAt;

    private Instant paidAt;
}
