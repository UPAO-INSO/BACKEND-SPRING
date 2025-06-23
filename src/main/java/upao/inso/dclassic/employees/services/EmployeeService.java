package upao.inso.dclassic.employees.services;

import upao.inso.dclassic.common.dto.PaginationRequestDto;
import upao.inso.dclassic.common.dto.PaginationResponseDto;
import upao.inso.dclassic.employees.model.EmployeeModel;

public interface EmployeeService {
    EmployeeModel create(EmployeeModel employee);
    PaginationResponseDto<EmployeeModel> findAll(PaginationRequestDto requestDto);
    EmployeeModel findById(Long id);
    EmployeeModel update(Long id, EmployeeModel employee);
    void delete(Long id);
    EmployeeModel findByUsername(String username);
    EmployeeModel findByEmail(String email);
}
