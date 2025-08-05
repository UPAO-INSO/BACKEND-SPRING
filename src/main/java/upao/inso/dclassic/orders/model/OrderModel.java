package upao.inso.dclassic.orders.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import upao.inso.dclassic.clients.model.ClientModel;
import upao.inso.dclassic.orders.enums.OrderStatus;
import upao.inso.dclassic.products.model.ProductOrderModel;
import upao.inso.dclassic.tables.model.TableModel;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@EntityListeners(AuditingEntityListener.class)
@Data @NoArgsConstructor @AllArgsConstructor
@Builder
@Entity
@Table(name = "orders")
public class OrderModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private  Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, columnDefinition = "varchar(20) default 'PENDING'")
    private OrderStatus orderStatus = OrderStatus.PENDING;

    @Column(nullable = false)
    private String comment;

    @Builder.Default
    @Column(nullable = false, columnDefinition = "boolean default false")
    private Boolean paid = false;

    @Column(nullable = false)
    private Integer totalItems;

    @Column(nullable = false)
    private Double totalPrice;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "table_id")
    private TableModel table;

    @ManyToOne(fetch =  FetchType.LAZY)
    @JoinColumn(name = "client_id")
    private ClientModel client;

    @Builder.Default
    @OneToMany(mappedBy = "order", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<ProductOrderModel> productOrders = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "order", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<OrderEmployeeModel> ordersEmployee = new ArrayList<>();

    @Column(nullable = false)
    @CreatedDate
    private Instant createdAt;

    @Column(nullable = false)
    @LastModifiedDate
    private Instant updatedAt;

    private Instant paidAt;
}
