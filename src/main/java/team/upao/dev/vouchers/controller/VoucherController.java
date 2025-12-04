package team.upao.dev.vouchers.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import team.upao.dev.common.dto.PaginationRequestDto;
import team.upao.dev.common.dto.PaginationResponseDto;
import team.upao.dev.vouchers.dto.VoucherRequestDto;
import team.upao.dev.vouchers.dto.VoucherResponseDto;
import team.upao.dev.vouchers.service.VoucherService;

@RestController
@RequestMapping("vouchers")
@RequiredArgsConstructor
public class VoucherController {
    private final VoucherService voucherService;

    @PostMapping
    public ResponseEntity<VoucherResponseDto> create(@RequestBody VoucherRequestDto voucherRequestDto) {
        return ResponseEntity.ok(voucherService.create(voucherRequestDto));
    }

    @GetMapping
    public ResponseEntity<PaginationResponseDto<VoucherResponseDto>> findAll(PaginationRequestDto paginationRequestDto) {
        return ResponseEntity.ok(voucherService.findAll(paginationRequestDto));
    }

    @PatchMapping("/{voucherId}/payment/{paymentId}")
    public ResponseEntity<VoucherResponseDto> associatePayment(
            @PathVariable Long voucherId,
            @PathVariable Long paymentId) {
        return ResponseEntity.ok(voucherService.associatePayment(voucherId, paymentId));
    }
}
