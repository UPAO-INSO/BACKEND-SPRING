package team.upao.dev.products.model;

import jakarta.persistence.*;
import lombok.*;

import team.upao.dev.inventory.model.InventoryModel;
import team.upao.dev.inventory.model.ProductInventoryModel;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "product", indexes = {
                @Index(name = "idx_product_name", columnList = "name"),
                @Index(name = "idx_product_type_id", columnList = "product_type_id")
})
public class ProductModel {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @Column(nullable = false)
        private String name;

        @Column(nullable = false)
        private String description;

        @Column(nullable = false)
        private Double price;

        @Column(nullable = false, columnDefinition = "BOOLEAN DEFAULT TRUE")
        @Builder.Default
        private Boolean active = true;

        @Column(nullable = false, columnDefinition = "BOOLEAN DEFAULT TRUE")
        @Builder.Default
        private Boolean available = true;

        @Column
        private String imageUrl;

        @ManyToOne
        @JoinColumn(name = "product_type_id", nullable = false)
        private ProductTypeModel productType;

        /**
         * Vínculo directo al registro de inventario para bebidas y descartables.
         * NULL para platos (que usan product_inventory para sus recetas).
         * Permite obtener el stock sin depender de coincidencia de nombres.
         */
        @ManyToOne(fetch = FetchType.EAGER)
        @JoinColumn(name = "inventory_id", nullable = true)
        private InventoryModel inventoryItem;

        @Builder.Default
        @OneToMany(mappedBy = "product", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
        private List<ProductOrderModel> productOrders = new ArrayList<>();

        // Relación con la tabla ProductInventoryModel
        @OneToMany(mappedBy = "product", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
        @Builder.Default
        private List<ProductInventoryModel> productInventory = new ArrayList<>();
}
