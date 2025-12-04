package team.upao.dev.jobs.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import team.upao.dev.employees.model.EmployeeModel;
import team.upao.dev.jobs.enums.JobEnum;

import java.util.ArrayList;
import java.util.List;

@Getter @Setter
@AllArgsConstructor @NoArgsConstructor
@Builder
@Entity
@Table(
        name = "job",
        indexes = @Index(name = "idx_job_title", columnList = "title", unique = true)
)
public class JobModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private JobEnum title;

    @Builder.Default
    @OneToMany(mappedBy = "job", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<EmployeeModel> employees = new ArrayList<>();
}
