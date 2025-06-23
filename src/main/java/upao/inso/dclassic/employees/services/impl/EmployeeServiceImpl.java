package upao.inso.dclassic.employees.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import upao.inso.dclassic.common.dto.PaginationRequestDto;
import upao.inso.dclassic.common.dto.PaginationResponseDto;
import upao.inso.dclassic.employees.model.EmployeeModel;
import upao.inso.dclassic.employees.repository.IEmployeeRepository;
import upao.inso.dclassic.employees.services.EmployeeService;

@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {
    private final IEmployeeRepository employeeRepository;

    @Override
    public EmployeeModel create(EmployeeModel employee) {
        return this.employeeRepository.save(employee);
    }

    @Override
    public PaginationResponseDto<EmployeeModel> findAll(PaginationRequestDto requestDto) {
        return null;
    }

    @Override
    public EmployeeModel findById(Long id) {
        return null;
    }

    @Override
    public EmployeeModel update(Long id, EmployeeModel employee) {
        return null;
    }

    @Override
    public void delete(Long id) {

    }

    @Override
    public EmployeeModel findByUsername(String username) {
        return null;
    }

    @Override
    public EmployeeModel findByEmail(String email) {
        return null;
    }
}
