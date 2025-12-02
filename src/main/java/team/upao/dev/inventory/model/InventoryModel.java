package team.upao.dev.inventory.model;
import team.upao.dev.inventory.enums.InventoryType;
import team.upao.dev.inventory.enums.UnitOfMeasure;
import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;

@Getter @Setter
@AllArgsConstructor @NoArgsConstructor
@Builder
@Entity
@Table(name = "inventory")
public class InventoryModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;  // Nombre del ítem en inventario (insumo, bebida, descartable)
   
    @Column(nullable = false)
    private BigDecimal quantity;  // Cantidad disponible en inventario

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private InventoryType type;  // INGREDIENT, BEVERAGE, DISPOSABLE 
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UnitOfMeasure unitOfMeasure; // Unidad de medida para los insumos (ej. kg, litro, unidad)

    // Getters y setters
}