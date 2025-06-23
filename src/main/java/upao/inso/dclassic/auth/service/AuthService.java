package upao.inso.dclassic.auth.service;

import upao.inso.dclassic.auth.dto.AuthResponseDto;
import upao.inso.dclassic.auth.dto.LoginDto;
import upao.inso.dclassic.auth.dto.RefreshTokenRequestDto;
import upao.inso.dclassic.auth.dto.SignupDto;

public interface AuthService {
    AuthResponseDto login(LoginDto request);

    void register(SignupDto request);

    AuthResponseDto refreshToken(RefreshTokenRequestDto token);
}
