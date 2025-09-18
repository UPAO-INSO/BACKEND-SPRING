package team.upao.dev.auth.service;

import team.upao.dev.auth.dto.AuthResponseDto;
import team.upao.dev.auth.dto.LoginDto;
import team.upao.dev.auth.dto.RefreshTokenRequestDto;
import team.upao.dev.auth.dto.SignupDto;

public interface AuthService {
    AuthResponseDto login(LoginDto request);
    void register(SignupDto request);
    AuthResponseDto refreshToken(RefreshTokenRequestDto token);
}
