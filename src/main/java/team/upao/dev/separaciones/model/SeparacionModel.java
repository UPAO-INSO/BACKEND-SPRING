package team.upao.dev.separaciones.model;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import team.upao.dev.customers.model.CustomerModel;
import team.upao.dev.pensionistas.model.PensionistaModel;
import team.upao.dev.separaciones.enums.SeparacionStatus;

import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@EntityListeners(AuditingEntityListener.class)
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
@Entity
@Table(
    name = "separacion",
    indexes = {
        @Index(name = "idx_separacion_date",           columnList = "date"),
        @Index(name = "idx_separacion_pensionista_id", columnList = "pensionista_id"),
        @Index(name = "idx_separacion_status",         columnList = "status")
    }
)
public class SeparacionModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @Builder.Default
    private LocalDate date = LocalDate.now();

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "pensionista_id")
    private PensionistaModel pensionista;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "customer_id")
    private CustomerModel customer;

    /** Nombre libre para clientes no registrados. */
    @Column(length = 255)
    private String clientName;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    @Builder.Default
    private SeparacionStatus status = SeparacionStatus.PENDIENTE;

    /**
     * Pensionista con créditos vigentes → plan_price_per_meal.
     * Cliente regular → precio calculado con las mismas reglas que OrderService.
     */
    @Column(nullable = false)
    @Builder.Default
    private Double totalPrice = 0.0;

    @Column(columnDefinition = "TEXT")
    private String notes;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    @LastModifiedDate
    @Column(nullable = false)
    private Instant updatedAt;

    @Builder.Default
    @OneToMany(mappedBy = "separacion", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<SeparacionItemModel> items = new ArrayList<>();
}
