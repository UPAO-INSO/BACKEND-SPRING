package team.upao.dev.employees.services;

import team.upao.dev.common.dto.PaginationRequestDto;
import team.upao.dev.common.dto.PaginationResponseDto;
import team.upao.dev.employees.dto.EmployeeDto;
import team.upao.dev.employees.model.EmployeeModel;

public interface EmployeeService {
    EmployeeDto create(EmployeeDto employee);
    PaginationResponseDto<EmployeeDto> findAll(PaginationRequestDto requestDto);
    EmployeeDto findById(Long id);
    EmployeeModel findModelById(Long id);
    EmployeeDto update(Long id, EmployeeDto employee);
    String delete(Long id);
}
