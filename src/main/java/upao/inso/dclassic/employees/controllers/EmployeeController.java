package upao.inso.dclassic.employees.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import upao.inso.dclassic.common.dto.PaginationRequestDto;
import upao.inso.dclassic.common.dto.PaginationResponseDto;
import upao.inso.dclassic.employees.dto.EmployeeDto;
import upao.inso.dclassic.employees.services.EmployeeService;

@RestController
@RequestMapping("employees")
@RequiredArgsConstructor
public class EmployeeController {
    private final EmployeeService employeeService;

    @PostMapping
    public ResponseEntity<EmployeeDto> create(@RequestBody @Valid EmployeeDto employee) {
        return ResponseEntity.ok(employeeService.create(employee));
    }

    @GetMapping
    public PaginationResponseDto<EmployeeDto> findAll(@ModelAttribute @Valid PaginationRequestDto paginationRequestDto) {
        return employeeService.findAll(paginationRequestDto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EmployeeDto> findById(@PathVariable Long id) {
        return ResponseEntity.ok(employeeService.findById(id));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<EmployeeDto> update(@PathVariable Long id, @RequestBody @Valid EmployeeDto employee) {
        return ResponseEntity.ok(employeeService.update(id, employee));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        return ResponseEntity.ok(employeeService.delete(id));
    }
}
