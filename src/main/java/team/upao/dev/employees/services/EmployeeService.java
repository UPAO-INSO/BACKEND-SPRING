package team.upao.dev.employees.services;

import team.upao.dev.common.dto.PaginationRequestDto;
import team.upao.dev.common.dto.PaginationResponseDto;
import team.upao.dev.employees.dto.EmployeeRequestDto;
import team.upao.dev.employees.model.EmployeeModel;

public interface EmployeeService {
    EmployeeRequestDto create(EmployeeRequestDto employee);
    PaginationResponseDto<EmployeeRequestDto> findAll(PaginationRequestDto requestDto);
    EmployeeRequestDto findById(Long id);
    EmployeeModel findModelById(Long id);
    EmployeeRequestDto update(Long id, EmployeeRequestDto employee);
    String delete(Long id);
}
