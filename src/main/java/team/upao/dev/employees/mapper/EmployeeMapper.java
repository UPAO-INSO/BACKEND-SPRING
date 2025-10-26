package team.upao.dev.employees.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import team.upao.dev.employees.dto.EmployeeRequestDto;
import team.upao.dev.employees.model.EmployeeModel;

import java.util.List;

@Component
@RequiredArgsConstructor
public class EmployeeMapper {
    public EmployeeModel toModel(EmployeeRequestDto employeeDto) {
        EmployeeModel employeeModel = new EmployeeModel();

        employeeModel.setName(employeeDto.getName());
        employeeModel.setLastname(employeeDto.getLastname());
        employeeModel.setPhone(employeeDto.getPhone());
        employeeModel.setSalary(employeeDto.getSalary());
        employeeModel.setJob(null);
        employeeModel.setUser(null);

        return employeeModel;
    }

    public List<EmployeeModel> toModel(List<EmployeeRequestDto> employees) {
        return employees.stream()
                .map(this::toModel)
                .toList();
    }

    public EmployeeRequestDto toDto(EmployeeModel employeeModel) {
        return EmployeeRequestDto.builder()
                .id(employeeModel.getId())
                .name(employeeModel.getName())
                .lastname(employeeModel.getLastname())
                .phone(employeeModel.getPhone())
                .salary(employeeModel.getSalary())
                .jobId(employeeModel.getJob().getId())
                .jobName(employeeModel.getJob().getTitle())
                .userId(employeeModel.getUser().getId())
                .username(employeeModel.getUser().getUsername())
                .createdAt(employeeModel.getCreatedAt())
                .updatedAt(employeeModel.getUpdatedAt())
                .build();
    }

    public List<EmployeeRequestDto> toDto(List<EmployeeModel> employees) {
        return employees.stream()
                .map(this::toDto)
                .toList();
    }
}
