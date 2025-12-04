package team.upao.dev.payments.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import team.upao.dev.common.dto.PaginationRequestDto;
import team.upao.dev.common.dto.PaginationResponseDto;
import team.upao.dev.payments.dto.PaymentRequestDto;
import team.upao.dev.payments.dto.PaymentResponseDto;
import team.upao.dev.payments.enums.PaymentType;
import team.upao.dev.payments.service.PaymentService;

@RestController
@RequiredArgsConstructor
@RequestMapping("payments")
public class PaymentController {
    private final PaymentService paymentService;

    @PostMapping
    public ResponseEntity<PaymentResponseDto> create(@RequestBody @Valid PaymentRequestDto paymentRequestDto) {
        return ResponseEntity.ok(paymentService.create(paymentRequestDto));
    }

    @GetMapping
    public ResponseEntity<PaginationResponseDto<PaymentResponseDto>> findAll(@ModelAttribute @Valid PaginationRequestDto paginationRequestDto,
                                                                             @RequestParam(value = "status", required = false) String status,
                                                                             @RequestParam(value = "paymentType", required = false) PaymentType paymentType) {
        return ResponseEntity.ok(paymentService.findAll(paginationRequestDto, status, paymentType));
    }

    @GetMapping("{id}")
    public ResponseEntity<PaymentResponseDto> findById(@PathVariable Long id) {
        return ResponseEntity.ok(paymentService.findById(id));
    }

    @PutMapping("{id}")
    public ResponseEntity<PaymentResponseDto> update(@PathVariable Long id, @RequestBody @Valid PaymentRequestDto paymentRequestDto) {
        return ResponseEntity.ok(paymentService.update(id, paymentRequestDto));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        return ResponseEntity.ok("Payment was deleted");
    }
}
