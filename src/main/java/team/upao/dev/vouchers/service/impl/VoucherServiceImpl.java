package team.upao.dev.vouchers.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.Nullable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import team.upao.dev.common.dto.PaginationRequestDto;
import team.upao.dev.common.dto.PaginationResponseDto;
import team.upao.dev.common.utils.PaginationUtils;
import team.upao.dev.exceptions.ResourceNotFoundException;
import team.upao.dev.payments.model.PaymentModel;
import team.upao.dev.payments.service.PaymentService;
import team.upao.dev.vouchers.dto.VoucherRequestDto;
import team.upao.dev.vouchers.dto.VoucherResponseDto;
import team.upao.dev.vouchers.mapper.VoucherMapper;
import team.upao.dev.vouchers.model.VoucherModel;
import team.upao.dev.vouchers.repository.IVoucherRepository;
import team.upao.dev.vouchers.service.VoucherService;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class VoucherServiceImpl implements VoucherService {
    private final IVoucherRepository voucherRepository;
    private final VoucherMapper voucherMapper;
    private final PaymentService paymentService;

    @Override
    @Transactional(readOnly = true)
    public @Nullable PaginationResponseDto<VoucherResponseDto> findAll(PaginationRequestDto paginationRequestDto) {
        final Pageable pageable = PaginationUtils.getPageable(paginationRequestDto);
        final Page<VoucherModel> entities = voucherRepository.findAll(pageable);
        final List<VoucherResponseDto> dtos = voucherMapper.toDto(entities.getContent());
        return PaginationResponseDto.<VoucherResponseDto>builder()
                .content(dtos)
                .page(entities.getNumber())
                .totalElements(entities.getTotalElements())
                .totalPages(entities.getTotalPages())
                .size(entities.getSize())
                .empty(entities.isEmpty())
                .build();
    }

    @Override
    @Transactional
    public @Nullable VoucherResponseDto create(VoucherRequestDto voucherRequestDto) {
        VoucherModel voucherModel = voucherMapper.toModel(voucherRequestDto);

        // Solo asociar payment si viene paymentId
        if (voucherRequestDto.getPaymentId() != null) {
            PaymentModel paymentModel = paymentService.findModelById(voucherRequestDto.getPaymentId());
            voucherModel.setPaymentModel(paymentModel);
        }

        VoucherModel savedVoucher = voucherRepository.save(voucherModel);
        log.info("Voucher created with id={}, paymentId={}", savedVoucher.getId(),
                savedVoucher.getPaymentModel() != null ? savedVoucher.getPaymentModel().getId() : "null");
        return voucherMapper.toDto(savedVoucher);
    }

    @Override
    @Transactional
    public @Nullable VoucherResponseDto associatePayment(Long voucherId, Long paymentId) {
        VoucherModel voucher = voucherRepository.findById(voucherId)
                .orElseThrow(() -> new ResourceNotFoundException("Voucher not found with id: " + voucherId));

        PaymentModel payment = paymentService.findModelById(paymentId);
        voucher.setPaymentModel(payment);

        VoucherModel savedVoucher = voucherRepository.save(voucher);
        log.info("Payment id={} associated to voucher id={}", paymentId, voucherId);
        return voucherMapper.toDto(savedVoucher);
    }
}
