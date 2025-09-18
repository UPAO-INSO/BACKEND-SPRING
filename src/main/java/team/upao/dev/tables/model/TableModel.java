package team.upao.dev.tables.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import team.upao.dev.tables.enums.TableStatus;

@Data @AllArgsConstructor @NoArgsConstructor
@Builder
@Entity
@Table(name = "tables")
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

    @Column(nullable = false)
    @Builder.Default
    private Boolean isActive = true;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private TableStatus status = TableStatus.AVAILABLE;
}
