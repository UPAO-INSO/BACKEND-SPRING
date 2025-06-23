package upao.inso.dclassic.employees.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import upao.inso.dclassic.employees.model.EmployeeModel;
import upao.inso.dclassic.employees.services.EmployeeService;

@RestController
@RequestMapping("employees")
@RequiredArgsConstructor
public class EmployeeController {
    private final EmployeeService employeeService;

    @PostMapping
    public ResponseEntity<EmployeeModel> create(EmployeeModel employee) {
        return ResponseEntity.ok(employeeService.create(employee));
    }
}
