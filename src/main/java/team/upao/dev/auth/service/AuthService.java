package team.upao.dev.auth.service;

import team.upao.dev.auth.dto.*;
import team.upao.dev.users.dto.UserDto;

public interface AuthService {
    AuthResponseDto login(LoginDto request);
    void register(SignupDto request);
    TokensResponseDto refreshToken(RefreshTokenRequestDto token);
    AuthResponseDto checkAuthStatus(UserDto user, String accessToken);


}
