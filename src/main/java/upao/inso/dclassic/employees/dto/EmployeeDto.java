package upao.inso.dclassic.employees.dto;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import upao.inso.dclassic.jobs.enums.JobEnum;
import upao.inso.dclassic.persons.dto.PersonDto;

@EqualsAndHashCode(callSuper = false)
@Data
@SuperBuilder
@AllArgsConstructor @NoArgsConstructor
public class EmployeeDto extends PersonDto {

    @NotNull(message = "Salary is required")
    @Positive
    private Double salary;

    @Positive
    private Long jobId;

    @Enumerated(EnumType.STRING)
    private JobEnum jobName;

    @Positive
    private Long userId;

    private String username;
}
