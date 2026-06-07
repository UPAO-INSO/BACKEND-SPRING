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

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("payments")
public class PaymentController {
    private final PaymentService paymentService;

    @PostMapping
    public ResponseEntity<PaymentResponseDto> create(@RequestBody @Valid PaymentRequestDto paymentRequestDto) {
        return ResponseEntity.status(org.springframework.http.HttpStatus.CREATED)
                .body(paymentService.create(paymentRequestDto));
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
        return ResponseEntity.ok(paymentService.delete(id));
    }

    /** Devuelve el pago asociado a una orden, o 404 si no existe */
    @GetMapping("by-order/{orderId}")
    public ResponseEntity<PaymentResponseDto> findByOrderId(@PathVariable UUID orderId) {
        return paymentService.findByOrderId(orderId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
