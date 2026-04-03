package team.upao.dev.integrations.culqi.client;

import org.springframework.http.ResponseEntity;
import team.upao.dev.integrations.culqi.dto.CulqiOrderRequestDto;

public interface CulqiProvider {
    ResponseEntity<Object> createOrder(CulqiOrderRequestDto orderRequest) throws Exception;
}
