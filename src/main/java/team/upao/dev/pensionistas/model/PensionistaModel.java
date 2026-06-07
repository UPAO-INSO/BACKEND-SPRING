package team.upao.dev.pensionistas.model;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import team.upao.dev.customers.model.CustomerModel;

import java.time.Instant;
import java.time.LocalDate;

@EntityListeners(AuditingEntityListener.class)
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
@Entity
@Table(
    name = "pensionista",
    indexes = {
        @Index(name = "idx_pensionista_customer_id", columnList = "customer_id"),
        @Index(name = "idx_pensionista_active",      columnList = "active")
    }
)
public class PensionistaModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "customer_id", nullable = false)
    private CustomerModel customer;

    /** Total de créditos comprados (ej. 30 almuerzos). */
    @Column(nullable = false)
    private Integer planCredits;

    /** Precio con descuento por almuerzo (ej. 9.00). */
    @Column(nullable = false)
    private Double planPricePerMeal;

    /** Monto total pagado upfront (planCredits × planPricePerMeal). */
    @Column(nullable = false)
    private Double planTotalPaid;

    /** Créditos disponibles; se decrementa en cada entrega. */
    @Column(nullable = false)
    private Integer creditsRemaining;

    @Column(nullable = false)
    private LocalDate startDate;

    @Column(nullable = false, columnDefinition = "BOOLEAN DEFAULT TRUE")
    @Builder.Default
    private Boolean active = true;

    @Column(columnDefinition = "TEXT")
    private String notes;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private Instant createdAt;
}
