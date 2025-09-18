package team.upao.dev.employees.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import team.upao.dev.jobs.model.JobModel;
import team.upao.dev.persons.model.PersonModel;
import team.upao.dev.users.model.UserModel;

@EntityListeners(AuditingEntityListener.class)
@EqualsAndHashCode(callSuper = true)
@Data @AllArgsConstructor @NoArgsConstructor
@SuperBuilder
@Entity
@PrimaryKeyJoinColumn(name = "id")
@Table(name = "employees")
public class EmployeeModel extends PersonModel {
    @Column(nullable = false)
    private Double salary;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "user_id")
    private UserModel user;

    @ManyToOne
    @JoinColumn(name = "job_id", nullable = false)
    private JobModel job;
}
