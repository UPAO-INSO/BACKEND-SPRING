package upao.inso.dclassic.employees.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import upao.inso.dclassic.employees.dto.EmployeeDto;
import upao.inso.dclassic.employees.model.EmployeeModel;

import java.util.List;

@Component
@RequiredArgsConstructor
public class EmployeeMapper {
    public EmployeeModel toModel(EmployeeDto employeeDto) {
        EmployeeModel employeeModel = new EmployeeModel();

        employeeModel.setName(employeeDto.getName());
        employeeModel.setLastname(employeeDto.getLastname());
        employeeModel.setPhone(employeeDto.getPhone());
        employeeModel.setSalary(employeeDto.getSalary());
        employeeModel.setJob(null);
        employeeModel.setUser(null);

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
                .createdAt(employeeModel.getCreatedAt())
                .updatedAt(employeeModel.getUpdatedAt())
                .build();
    }

    public List<EmployeeDto> toDto(List<EmployeeModel> employees) {
        return employees.stream()
                .map(this::toDto)
                .toList();
    }
}
