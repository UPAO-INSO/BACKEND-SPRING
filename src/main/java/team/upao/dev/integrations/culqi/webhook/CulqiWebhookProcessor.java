package team.upao.dev.integrations.culqi.webhook;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import team.upao.dev.exceptions.ResourceNotFoundException;
import team.upao.dev.payments.dto.PaymentRequestDto;
import team.upao.dev.payments.dto.PaymentResponseDto;
import team.upao.dev.payments.mapper.PaymentMapper;
import team.upao.dev.payments.model.PaymentModel;
import team.upao.dev.payments.service.PaymentService;

import java.util.UUID;

@Component
@RequiredArgsConstructor
@Slf4j
public class CulqiWebhookProcessor {
    private final PaymentMapper paymentMapper;
    private final PaymentService paymentService;

    public void process(Object payloadObj, String rawBody) {
        try {
            PaymentRequestDto dto = paymentMapper.toPaymentRequestDto(payloadObj, rawBody);
            if (dto == null) {
                throw  new ResourceNotFoundException("Payload no mapeable a PaymentRequestDto, se omite.");
            }

            UUID orderId = dto.getOrderId();
            if (orderId == null) {
                throw new IllegalArgumentException("OrderId no puede ser nulo");
            }

            PaymentModel existing = paymentService.findModelByOrderIdInPayment(orderId);
            existing.setProvider(dto.getProvider());
            existing.setExternalId(dto.getExternalId());
            existing.setAmount(dto.getAmount());
            existing.setCurrencyCode(dto.getCurrencyCode());
            existing.setDescription(dto.getDescription());
            existing.setState(dto.getState());
            existing.setTotalFee(dto.getTotalFee());
            existing.setNetAmount(dto.getNetAmount());
            existing.setQr(dto.getQr());
            existing.setUrlPe(dto.getUrlPe());
            existing.setCreationDate(dto.getCreationDate());
            existing.setExpirationDate(dto.getExpirationDate());
            existing.setUpdatedAt(dto.getUpdatedAt());
            existing.setPaidAt(dto.getPaidAt());
            existing.setRawResponse(dto.getRawResponse());
            PaymentResponseDto saved = paymentService.update(existing);
            log.info("Pago actualizado para orderId={} paymentId={}", orderId, saved.getId());
        } catch (Exception e) {
            log.error("Error procesando webhook para persistir pago", e);
        }
    }
}
