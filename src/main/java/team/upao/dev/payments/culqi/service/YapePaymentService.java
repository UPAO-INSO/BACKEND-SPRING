package team.upao.dev.payments.culqi.service;

import team.upao.dev.payments.culqi.dto.CreateYapeChargeRequestDto;
import team.upao.dev.payments.culqi.dto.CreateYapeTokenRequestDto;
import team.upao.dev.payments.culqi.dto.CreateYapeTokenResponseDto;
import team.upao.dev.payments.culqi.dto.YapeChargeResponseDto;

public interface YapePaymentService {
    CreateYapeTokenResponseDto createToken(CreateYapeTokenRequestDto requestDto);
    YapeChargeResponseDto createCharge(CreateYapeChargeRequestDto requestDto);
}
