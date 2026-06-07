package team.upao.dev.menu.model;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import team.upao.dev.products.model.ProductModel;

import java.time.Instant;
import java.time.LocalDate;

@EntityListeners(AuditingEntityListener.class)
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
@Entity
@Table(
    name = "menu_diario_item",
    indexes = {
        @Index(name = "idx_mdi_date", columnList = "date")
    },
    uniqueConstraints = {
        @UniqueConstraint(name = "uq_mdi_product_date", columnNames = {"product_id", "date"})
    }
)
public class MenuDiarioItemModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "product_id", nullable = false)
    private ProductModel product;

    @Column(nullable = false)
    @Builder.Default
    private LocalDate date = LocalDate.now();

    /** Porciones preparadas para el día. */
    @Column(nullable = false)
    private Integer estimatedPortions;

    /** Se incrementa al crear un pedido o separación con este producto. */
    @Column(nullable = false)
    @Builder.Default
    private Integer usedPortions = 0;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    public Integer getRemainingPortions() {
        return estimatedPortions - usedPortions;
    }

    public boolean isSoldOut() {
        return usedPortions >= estimatedPortions;
    }
}
