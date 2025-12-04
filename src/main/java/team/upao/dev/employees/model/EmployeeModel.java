package team.upao.dev.employees.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import team.upao.dev.jobs.model.JobModel;
import team.upao.dev.persons.model.PersonModel;
import team.upao.dev.users.model.UserModel;

@EntityListeners(AuditingEntityListener.class)
@Getter @Setter
@AllArgsConstructor @NoArgsConstructor
@SuperBuilder
@Entity
@PrimaryKeyJoinColumn(name = "id")
@Table(
        name = "employees",
        indexes = {
                @Index(name = "idx_employees_user_id", columnList = "user_id"),
                @Index(name = "idx_employees_job_id", columnList = "job_id")
        }
)
public class EmployeeModel extends PersonModel {
    @Column(nullable = false)
    private Double salary;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserModel user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "job_id", nullable = false)
    private JobModel job;
}
