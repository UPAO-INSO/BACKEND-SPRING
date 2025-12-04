package team.upao.dev.payments.service;

import team.upao.dev.common.dto.PaginationRequestDto;
import team.upao.dev.common.dto.PaginationResponseDto;
import team.upao.dev.payments.dto.PaymentRequestDto;
import team.upao.dev.payments.dto.PaymentResponseDto;
import team.upao.dev.payments.enums.PaymentType;
import team.upao.dev.payments.model.PaymentModel;

import java.util.List;
import java.util.UUID;

public interface PaymentService {
    PaymentResponseDto create(PaymentRequestDto paymentResponseDto);
    PaymentResponseDto findById(Long id);
    PaymentModel findModelById(Long id);
    PaymentModel findModelByOrderIdInPayment(UUID orderId);
    PaginationResponseDto<PaymentResponseDto> findAll(PaginationRequestDto paginationRequestDto, String status, PaymentType paymentType);
    PaymentResponseDto update(Long id, PaymentRequestDto paymentResponseDto);
    PaymentResponseDto update(PaymentModel paymentModel);
    String delete(Long id);
}
