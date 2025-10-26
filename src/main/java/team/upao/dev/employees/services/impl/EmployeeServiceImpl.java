package team.upao.dev.employees.services.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import team.upao.dev.common.dto.PaginationRequestDto;
import team.upao.dev.common.dto.PaginationResponseDto;
import team.upao.dev.common.utils.PaginationUtils;
import team.upao.dev.employees.dto.EmployeeRequestDto;
import team.upao.dev.employees.mapper.EmployeeMapper;
import team.upao.dev.employees.model.EmployeeModel;
import team.upao.dev.employees.repository.IEmployeeRepository;
import team.upao.dev.employees.services.EmployeeService;
import team.upao.dev.jobs.service.JobService;
import team.upao.dev.users.service.UserService;

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
    public EmployeeRequestDto create(EmployeeRequestDto employee) {
        EmployeeModel employeeModel = employeeMapper.toModel(employee);

        employeeModel.setJob(jobService.findById(employee.getJobId()));
        employeeModel.setUser(userService.findModelById(employee.getUserId()));

        return this.employeeMapper.toDto(employeeRepository.save(employeeModel));
    }

    @Override
    public PaginationResponseDto<EmployeeRequestDto> findAll(PaginationRequestDto requestDto) {
        final Pageable pageable = PaginationUtils.getPageable(requestDto);
        final Page<EmployeeModel> entities = this.employeeRepository.findAll(pageable);
        final List<EmployeeRequestDto> employeeDtos = employeeMapper.toDto(entities.getContent());
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
    public EmployeeRequestDto findById(Long id) {
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
    public EmployeeRequestDto update(Long id, EmployeeRequestDto employee) {
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
