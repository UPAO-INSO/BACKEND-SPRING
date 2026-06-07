package team.upao.dev.pensionistas.model;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import team.upao.dev.separaciones.model.SeparacionModel;

import java.time.Instant;
import java.time.LocalDate;

@EntityListeners(AuditingEntityListener.class)
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
@Entity
@Table(
    name = "pensionista_consumo",
    indexes = {
        @Index(name = "idx_pc_pensionista_id", columnList = "pensionista_id"),
        @Index(name = "idx_pc_date",           columnList = "date")
    }
)
public class PensionistaConsumoModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pensionista_id", nullable = false)
    private PensionistaModel pensionista;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "separacion_id")
    private SeparacionModel separacion;

    @Column(nullable = false)
    @Builder.Default
    private LocalDate date = LocalDate.now();

    /** Precio del plan vigente al momento del consumo. */
    @Column(nullable = false)
    private Double priceApplied;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private Instant createdAt;
}
