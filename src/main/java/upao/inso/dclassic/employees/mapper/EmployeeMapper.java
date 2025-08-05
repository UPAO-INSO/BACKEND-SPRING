package upao.inso.dclassic.employees.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import upao.inso.dclassic.employees.dto.EmployeeDto;
import upao.inso.dclassic.employees.model.EmployeeModel;
import upao.inso.dclassic.jobs.model.JobModel;
import upao.inso.dclassic.jobs.service.JobService;
import upao.inso.dclassic.users.model.UserModel;
import upao.inso.dclassic.users.service.UserService;

import java.util.List;

@Component
@RequiredArgsConstructor
public class EmployeeMapper {
    private final UserService userService;
    private final JobService jobService;

    public EmployeeModel toModel(EmployeeDto employeeDto) {
        EmployeeModel employeeModel = new EmployeeModel();

        JobModel job = jobService.findById(employeeDto.getJobId());
        UserModel user = userService.findModelById(employeeDto.getUserId());

        employeeModel.setName(employeeDto.getName());
        employeeModel.setLastname(employeeDto.getLastname());
        employeeModel.setPhone(employeeDto.getPhone());
        employeeModel.setSalary(employeeDto.getSalary());
        employeeModel.setJob(job);
        employeeModel.setUser(user);

        return employeeModel;
    }

    public List<EmployeeModel> toModel(List<EmployeeDto> employees) {
        return employees.stream()
                .map(this::toModel)
                .toList();
    }

    public EmployeeDto toDto(EmployeeModel employeeModel) {
        return EmployeeDto.builder()
                .id(employeeModel.getId())
                .name(employeeModel.getName())
                .lastname(employeeModel.getLastname())
                .phone(employeeModel.getPhone())
                .salary(employeeModel.getSalary())
                .jobId(employeeModel.getJob().getId())
                .jobName(employeeModel.getJob().getTitle())
                .userId(employeeModel.getUser().getId())
                .username(employeeModel.getUser().getUsername())
                .build();
    }

    public List<EmployeeDto> toDto(List<EmployeeModel> employees) {
        return employees.stream()
                .map(this::toDto)
                .toList();
    }
}
