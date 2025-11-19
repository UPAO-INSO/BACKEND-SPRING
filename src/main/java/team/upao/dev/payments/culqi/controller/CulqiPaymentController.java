package team.upao.dev.payments.culqi.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import team.upao.dev.payments.culqi.dto.CreateYapeChargeRequestDto;
import team.upao.dev.payments.culqi.dto.CreateYapeTokenRequestDto;
import team.upao.dev.payments.culqi.dto.CreateYapeTokenResponseDto;
import team.upao.dev.payments.culqi.dto.YapeChargeResponseDto;
import team.upao.dev.payments.culqi.service.YapePaymentService;

@RestController
@RequestMapping("/payments/culqi")
@RequiredArgsConstructor
@Validated
public class CulqiPaymentController {
    private final YapePaymentService yapePaymentService;

    @PostMapping("/yape/token")
    public ResponseEntity<CreateYapeTokenResponseDto> generateYapeToken(
            @Valid @RequestBody CreateYapeTokenRequestDto requestDto) {
        CreateYapeTokenResponseDto response = yapePaymentService.createToken(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/yape/charge")
    public ResponseEntity<YapeChargeResponseDto> createYapeCharge(
            @Valid @RequestBody CreateYapeChargeRequestDto requestDto) {
        YapeChargeResponseDto response = yapePaymentService.createCharge(requestDto);
        return ResponseEntity.ok(response);
    }
}
