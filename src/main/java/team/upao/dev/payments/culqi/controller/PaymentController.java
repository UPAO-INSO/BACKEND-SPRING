package team.upao.dev.payments.culqi.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import team.upao.dev.payments.culqi.dto.CulqiOrderRequestDto;
import team.upao.dev.payments.culqi.service.PaymentService;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/payments/culqi")
@RequiredArgsConstructor
@Validated
@Tag(name = "Pagos Culqi", description = "API para procesar pagos con Culqi (Yape y otras billeteras digitales)")
public class PaymentController {
    private final PaymentService paymentService;

    @PostMapping("/createOrder")
    public ResponseEntity<Object> createOrder(@RequestBody @Valid CulqiOrderRequestDto orderRequest) throws Exception {
        return paymentService.createOrder(orderRequest);
    }

    @PostMapping("/confirmOrder/{orderId}")
    public ResponseEntity<Object> confirmOrder(@PathVariable String orderId) throws Exception {
        return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body("Implementar confirmOrder en CulqiProvider");
    }

    @PostMapping("/webhook")
    public ResponseEntity<Void> webhook(@RequestBody Map<String, Object> payload) {
        log.info("Webhook Culqi recibido => {}", payload);
        return ResponseEntity.ok().build();
    }
}
