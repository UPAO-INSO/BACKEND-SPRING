package team.upao.dev.payments.culqi.service;

import org.springframework.http.ResponseEntity;
import team.upao.dev.payments.culqi.dto.CulqiOrderRequestDto;

public interface PaymentService {
    ResponseEntity<Object> createOrder(CulqiOrderRequestDto orderRequest) throws Exception;
}
