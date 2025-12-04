package team.upao.dev.payments.service.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import team.upao.dev.common.dto.PaginationRequestDto;
import team.upao.dev.common.dto.PaginationResponseDto;
import team.upao.dev.common.utils.PaginationUtils;
import team.upao.dev.customers.model.CustomerModel;
import team.upao.dev.customers.service.CustomerService;
import team.upao.dev.exceptions.ResourceNotFoundException;
import team.upao.dev.orders.model.OrderModel;
import team.upao.dev.orders.service.OrderService;
import team.upao.dev.payments.dto.PaymentRequestDto;
import team.upao.dev.payments.dto.PaymentResponseDto;
import team.upao.dev.payments.enums.PaymentType;
import team.upao.dev.payments.mapper.PaymentMapper;
import team.upao.dev.payments.model.PaymentModel;
import team.upao.dev.payments.repository.IPaymentRepository;
import team.upao.dev.payments.service.PaymentService;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {
    private final OrderService orderService;
    private final CustomerService customerService;
    private final IPaymentRepository paymentRepository;
    private final PaymentMapper paymentMapper;

    @PersistenceContext
    private EntityManager em;

    @Override
    @Transactional
    public PaymentResponseDto create(PaymentRequestDto paymentRequestDto) {
        PaymentModel paymentModel = paymentMapper.toModel(paymentRequestDto);

        OrderModel orderModel = orderService.findModelByOrderIdInPayment(paymentRequestDto.getOrderId());
        CustomerModel customerModel = customerService.findModelByCustomerIdInPayment(paymentRequestDto.getCustomerId());
        paymentModel.setOrder(orderModel);
        paymentModel.setCustomer(customerModel);

        Integer incomingAmount = paymentRequestDto.getAmount();

        String provider = paymentRequestDto.getProvider();
        if (provider != null && provider.equalsIgnoreCase("culqi")) {
            paymentModel.setAmount(incomingAmount);
        } else {
            BigDecimal amountBd = BigDecimal.valueOf(incomingAmount);
            BigDecimal amountInCentsBd = amountBd.multiply(BigDecimal.valueOf(100));
            BigDecimal taxCentsBd = amountInCentsBd.multiply(new BigDecimal("0.18"));

            int amountInCents = amountInCentsBd.setScale(0, RoundingMode.HALF_UP).intValueExact();
            int taxCents = taxCentsBd.setScale(0, RoundingMode.HALF_UP).intValueExact();

            log.info("Payment.create - incomingAmount (soles) = {}, amountInCents = {}, taxCents = {}",
                    incomingAmount, amountInCents, taxCents);

            paymentModel.setAmount(amountInCents);
        }

        PaymentModel savedPayment = paymentRepository.save(paymentModel);
        return paymentMapper.toDto(savedPayment);
    }



    @Override
    public PaymentResponseDto findById(Long id) {
        PaymentModel paymentModel = paymentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Payment not found with id: " + id));
        return paymentMapper.toDto(paymentModel);
    }

    @Override
    public PaymentModel findModelById(Long id) {
        return paymentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Payment not found with id: " + id));
    }

    @Override
    public PaymentModel findModelByOrderIdInPayment(UUID orderId) {
        return paymentRepository.findByOrderId(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Payment not found for orderId: " + orderId));
    }

    @Override
    @Transactional(readOnly = true)
    public PaginationResponseDto<PaymentResponseDto> findAll(PaginationRequestDto paginationRequestDto, String status, PaymentType paymentType) {
        final Pageable pageable = PaginationUtils.getPageable(paginationRequestDto);
        final Page<PaymentModel> entities;

        if (status != null && paymentType != null) {
            entities = paymentRepository.findAllByStateAndPaymentType(pageable, status, paymentType);
        } else if (status != null) {
            entities = paymentRepository.findAllByState(pageable, status);
        } else if (paymentType != null) {
            entities = paymentRepository.findAllByPaymentType(pageable, paymentType);
        } else {
            entities = paymentRepository.findAll(pageable);
        }

        final List<PaymentResponseDto> dtos = paymentMapper.toDto(entities.getContent());
        return PaginationResponseDto.<PaymentResponseDto>builder()
                .content(dtos)
                .totalPages(entities.getTotalPages())
                .totalElements(entities.getTotalElements())
                .page(entities.getNumber() + 1)
                .size(entities.getSize())
                .empty(entities.isEmpty())
                .build();
    }

    @Override
    public PaymentResponseDto update(Long id, PaymentRequestDto paymentResponseDto) {
        return null;
    }

    @Override
    public PaymentResponseDto update(PaymentModel paymentModel) {
        PaymentModel saved = paymentRepository.save(paymentModel);
        return paymentMapper.toDto(saved);
    }

    @Override
    public String delete(Long id) {
        return "";
    }
}
