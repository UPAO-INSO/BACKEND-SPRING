package upao.inso.dclassic.auth.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import upao.inso.dclassic.auth.dto.AuthResponseDto;
import upao.inso.dclassic.auth.dto.LoginDto;
import upao.inso.dclassic.auth.dto.RefreshTokenRequestDto;
import upao.inso.dclassic.auth.dto.SignupDto;
import upao.inso.dclassic.auth.service.AuthService;

@Slf4j
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDto> login(@Valid @RequestBody LoginDto request) {
        return new ResponseEntity<>(authService.login(request), HttpStatus.OK);
    }

    @PostMapping("/register")
    public ResponseEntity<Object> register(@Valid @RequestBody SignupDto request) {
        authService.register(request);
        return ResponseEntity.ok("User registered successfully");
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<AuthResponseDto> refreshToken(@Valid @RequestHeader(HttpHeaders.AUTHORIZATION) RefreshTokenRequestDto refreshToken) {
        return ResponseEntity.ok(authService.refreshToken(refreshToken));
    }
}
