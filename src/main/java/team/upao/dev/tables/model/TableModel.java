package team.upao.dev.tables.model;

import jakarta.persistence.*;
import lombok.*;
import team.upao.dev.tables.enums.TableStatus;

@Getter @Setter
@AllArgsConstructor @NoArgsConstructor
@Builder
@Entity
@Table(
        name = "tables",
        indexes = {
                @Index(name = "idx_tables_number", columnList = "number"),
                @Index(name = "idx_tables_floor", columnList = "floor"),
                @Index(name = "idx_tables_status", columnList = "status")
        }
)
public class TableModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String  number;

    @Column(nullable = false)
    private Integer capacity;

    @Column(nullable = false)
    private Integer floor;

    @Column(nullable = false, columnDefinition = "BOOLEAN DEFAULT TRUE")
    @Builder.Default
    private Boolean isActive = true;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private TableStatus status = TableStatus.AVAILABLE;
}
