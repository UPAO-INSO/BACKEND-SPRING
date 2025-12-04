package team.upao.dev.products.model;

import jakarta.persistence.*;
import lombok.*;
import team.upao.dev.products.enums.ProductOrderItemStatus;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(
        name = "product_order_item",
        indexes = {
                @Index(name = "idx_poi_product_order_id", columnList = "product_order_id")
        }
)
public class ProductOrderItemModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Integer quantity;

    @Column(nullable = false)
    @Builder.Default
    private Integer preparedQuantity = 0;

    @Column(nullable = false)
    @Builder.Default
    private Integer servedQuantity = 0;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private ProductOrderItemStatus status = ProductOrderItemStatus.PENDING;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_order_id", nullable = false)
    private ProductOrderModel productOrder;
}
