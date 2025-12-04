package team.upao.dev.integrations.culqi.service;

import org.springframework.http.ResponseEntity;
import team.upao.dev.integrations.culqi.dto.CulqiOrderRequestDto;

public interface CulqiService {
    ResponseEntity<Object> createOrder(CulqiOrderRequestDto orderRequest) throws Exception;
}
