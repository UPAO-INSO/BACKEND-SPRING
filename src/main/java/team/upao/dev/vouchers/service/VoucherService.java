package team.upao.dev.vouchers.service;

import org.jspecify.annotations.Nullable;
import team.upao.dev.common.dto.PaginationRequestDto;
import team.upao.dev.common.dto.PaginationResponseDto;
import team.upao.dev.vouchers.dto.VoucherRequestDto;
import team.upao.dev.vouchers.dto.VoucherResponseDto;

public interface VoucherService {
    @Nullable PaginationResponseDto<VoucherResponseDto> findAll(PaginationRequestDto paginationRequestDto);

    @Nullable VoucherResponseDto create(VoucherRequestDto voucherRequestDto);

    @Nullable VoucherResponseDto associatePayment(Long voucherId, Long paymentId);
}
