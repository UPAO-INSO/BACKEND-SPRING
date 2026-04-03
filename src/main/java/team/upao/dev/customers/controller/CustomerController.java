package team.upao.dev.customers.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import team.upao.dev.customers.dto.CustomerRequestRequestDto;
import team.upao.dev.customers.dto.CustomerResponseDto;
import team.upao.dev.customers.service.CustomerService;
import team.upao.dev.common.controller.BaseController;
import team.upao.dev.common.service.BaseService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("customers")
public class CustomerController extends BaseController<CustomerRequestRequestDto, CustomerResponseDto, Long> {
    private final CustomerService customerService;

    @Override
    protected BaseService<CustomerRequestRequestDto, CustomerResponseDto, Long> getService() {
        return customerService;
    }

    @GetMapping("/email/{email}")
    public CustomerResponseDto findByEmail(@PathVariable String email) {
        return customerService.findByEmail(email);
    }

    @GetMapping("/phone/{phone}")
    public CustomerResponseDto findByPhone(@PathVariable String phone) {
        return customerService.findByPhone(phone);
    }

    @GetMapping("/document/{document}")
    public ResponseEntity<List<CustomerResponseDto>> findByDocument(@PathVariable String document) {
        return ResponseEntity.ok(customerService.findByDocument(document));
    }
}
