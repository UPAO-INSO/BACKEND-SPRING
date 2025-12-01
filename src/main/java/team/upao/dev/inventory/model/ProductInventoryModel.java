package team.upao.dev.inventory.model;
import team.upao.dev.products.model.ProductModel;
import java.math.BigDecimal;

import jakarta.persistence.*;
import lombok.*;

/* El fin de esta tabla básicamente es asociar los insumos a sus respectivos platos (la receta básicamente)
   También tiene un fin práctico que es asociar las bebidas y descartables de la tabla productos (donde no se registran cantidades)
   con su stock/cantidades/existencias en el inventario.
*/
@Getter @Setter
@AllArgsConstructor @NoArgsConstructor
@Builder
@Entity
@Table(name = "product_inventory")
public class ProductInventoryModel { 
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private BigDecimal quantity;  // Cantidad del ingrediente usado en la receta (ej. 0.5 kg de harina)

    @Column(nullable = false)
    private String unitOfMeasure;  // Unidad de medida del ingrediente (ej. kg, litro, unidad)
    
    @ManyToOne
    @JoinColumn(name = "product_id", referencedColumnName = "id", nullable = false)
    private ProductModel product;  // El plato o producto (ej. pizza, ensalada, bebida, descartable)

    @ManyToOne
    @JoinColumn(name = "inventory_id", referencedColumnName = "id", nullable = false)
    private InventoryModel inventory;  // El insumo usado en un plato o la bebida o descartable ordenado (referencia a `inventory`)

    // Getters y setters
}