package team.upao.dev.payments.culqi.client;

import org.springframework.http.ResponseEntity;
import team.upao.dev.payments.culqi.dto.CulqiOrderRequestDto;

public interface CulqiProvider {
    ResponseEntity<Object> createOrder(CulqiOrderRequestDto orderRequest) throws Exception;
}
