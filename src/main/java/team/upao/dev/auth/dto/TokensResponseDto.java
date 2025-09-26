package team.upao.dev.auth.dto;

import lombok.Builder;

@Builder
public record TokensResponseDto (String accessToken, String refreshToken) {}
