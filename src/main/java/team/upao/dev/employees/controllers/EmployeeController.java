package team.upao.dev.employees.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import team.upao.dev.common.dto.PaginationRequestDto;
import team.upao.dev.common.dto.PaginationResponseDto;
import team.upao.dev.employees.dto.EmployeeRequestDto;
import team.upao.dev.employees.services.EmployeeService;

@RestController
@RequestMapping("employees")
@RequiredArgsConstructor
public class EmployeeController {
    private final EmployeeService employeeService;

    @PostMapping
    public ResponseEntity<EmployeeRequestDto> create(@RequestBody @Valid EmployeeRequestDto employee) {
        return ResponseEntity.ok(employeeService.create(employee));
    }

    @GetMapping
    public PaginationResponseDto<EmployeeRequestDto> findAll(@ModelAttribute @Valid PaginationRequestDto paginationRequestDto) {
        return employeeService.findAll(paginationRequestDto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EmployeeRequestDto> findById(@PathVariable Long id) {
        return ResponseEntity.ok(employeeService.findById(id));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<EmployeeRequestDto> update(@PathVariable Long id, @RequestBody @Valid EmployeeRequestDto employee) {
        return ResponseEntity.ok(employeeService.update(id, employee));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        return ResponseEntity.ok(employeeService.delete(id));
    }
}
