package upao.inso.dclassic.tables.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import upao.inso.dclassic.tables.enums.TableStatus;

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
    private String capacity;

    @Column(nullable = false)
    private Integer floor;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TableStatus status;
}
