package team.upao.dev.separaciones.model;

import jakarta.persistence.*;
import lombok.*;
import team.upao.dev.products.model.ProductModel;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
@Entity
@Table(
    name = "separacion_item",
    indexes = {
        @Index(name = "idx_separacion_item_separacion_id", columnList = "separacion_id"),
        @Index(name = "idx_separacion_item_product_id",    columnList = "product_id")
    }
)
public class SeparacionItemModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "separacion_id", nullable = false)
    private SeparacionModel separacion;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "product_id", nullable = false)
    private ProductModel product;

    @Column(nullable = false)
    private Integer quantity;

    /** 0 si el ítem está cubierto por el plan del pensionista. */
    @Column(nullable = false)
    @Builder.Default
    private Double unitPrice = 0.0;
}
