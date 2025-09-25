package team.upao.dev.auth.dto;

import team.upao.dev.users.dto.UserResponseDto;

public record AuthResponseDto(UserResponseDto user, TokensResponseDto tokens) {}
