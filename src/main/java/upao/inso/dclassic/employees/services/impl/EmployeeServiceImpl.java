package upao.inso.dclassic.employees.services.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import upao.inso.dclassic.common.dto.PaginationRequestDto;
import upao.inso.dclassic.common.dto.PaginationResponseDto;
import upao.inso.dclassic.common.utils.PaginationUtils;
import upao.inso.dclassic.employees.dto.EmployeeDto;
import upao.inso.dclassic.employees.mapper.EmployeeMapper;
import upao.inso.dclassic.employees.model.EmployeeModel;
import upao.inso.dclassic.employees.repository.IEmployeeRepository;
import upao.inso.dclassic.employees.services.EmployeeService;
import upao.inso.dclassic.jobs.service.JobService;
import upao.inso.dclassic.users.service.UserService;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {
    private final IEmployeeRepository employeeRepository;
    private final EmployeeMapper employeeMapper;
    private final JobService jobService;
    private final UserService userService;

    @Override
    @Transactional
    public EmployeeDto create(EmployeeDto employee) {
        EmployeeModel employeeModel = employeeMapper.toModel(employee);

        employeeModel.setJob(jobService.findById(employee.getJobId()));
        employeeModel.setUser(userService.findModelById(employee.getUserId()));
//        EmployeeModel saved = employeeRepository.save(employeeModel);

        return this.employeeMapper.toDto(employeeModel);
    }

    @Override
    public PaginationResponseDto<EmployeeDto> findAll(PaginationRequestDto requestDto) {
        final Pageable pageable = PaginationUtils.getPageable(requestDto);
        final Page<EmployeeModel> entities = this.employeeRepository.findAll(pageable);
        final List<EmployeeDto> employeeDtos = employeeMapper.toDto(entities.getContent());
        return new PaginationResponseDto<>(
                employeeDtos,
                entities.getTotalPages(),
                entities.getTotalElements(),
                entities.getSize(),
                entities.getNumber()+1,
                entities.isEmpty()
        );
    }

    @Override
    public EmployeeDto findById(Long id) {
        EmployeeModel employee = employeeRepository
                .findById(id)
                .orElseThrow(() -> new RuntimeException("Employee not found with id: " + id));

        return employeeMapper.toDto(employee);
    }

    @Override
    public EmployeeModel findModelById(Long id) {
        return employeeRepository
                .findById(id)
                .orElseThrow(() -> new RuntimeException("Employee not found with id: " + id));
    }

    @Override
    @Transactional
    public EmployeeDto update(Long id, EmployeeDto employee) {
        EmployeeModel employeeModel = findModelById(id);

        employeeModel.setName(employee.getName());
        employeeModel.setLastname(employee.getLastname());
        employeeModel.setPhone(employee.getPhone());
        employeeModel.setSalary(employee.getSalary());
        employeeModel.getJob().setId(employee.getJobId());
        employeeModel.getUser().setId(employee.getUserId());

        this.employeeRepository.save(employeeModel);

        return employeeMapper.toDto(employeeModel);
    }

    @Override
    @Transactional
    public String delete(Long id) {
        return "Employee with ID " + id + " deleted successfully.";
    }
}
