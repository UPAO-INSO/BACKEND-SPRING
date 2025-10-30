package team.upao.dev.employees.dto;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;
import lombok.experimental.SuperBuilder;
import team.upao.dev.jobs.enums.JobEnum;
import team.upao.dev.persons.dto.PersonRequestDto;

@Getter @Setter
@SuperBuilder
@AllArgsConstructor @NoArgsConstructor
public class EmployeeRequestDto extends PersonRequestDto {

    @NotNull(message = "Salary is required")
    @Positive
    private Double salary;

    private Long jobId;

    @Enumerated(EnumType.STRING)
    private JobEnum jobName;

    @Positive
    private Long userId;

    private String username;
}
