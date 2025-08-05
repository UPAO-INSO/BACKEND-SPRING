package upao.inso.dclassic.employees.services;

import upao.inso.dclassic.common.dto.PaginationRequestDto;
import upao.inso.dclassic.common.dto.PaginationResponseDto;
import upao.inso.dclassic.employees.dto.EmployeeDto;
import upao.inso.dclassic.employees.model.EmployeeModel;

public interface EmployeeService {
    EmployeeDto create(EmployeeDto employee);
    PaginationResponseDto<EmployeeDto> findAll(PaginationRequestDto requestDto);
    EmployeeDto findById(Long id);
    EmployeeModel findModelById(Long id);
    EmployeeDto update(Long id, EmployeeDto employee);
    String delete(Long id);
}
