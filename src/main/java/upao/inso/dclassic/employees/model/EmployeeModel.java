package upao.inso.dclassic.employees.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import upao.inso.dclassic.jobs.model.JobModel;
import upao.inso.dclassic.persons.model.PersonModel;
import upao.inso.dclassic.users.model.UserModel;

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
