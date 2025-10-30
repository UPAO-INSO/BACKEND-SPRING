package team.upao.dev.auth.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import team.upao.dev.auth.dto.*;
import team.upao.dev.auth.service.AuthService;
import team.upao.dev.users.dto.UserDto;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @GetMapping("/check-status")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<AuthResponseDto> checkAuthStatus(
            Authentication authentication,
            HttpServletRequest request) {

        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        String accessToken = "";
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            accessToken = authHeader.substring(7);
        }

        UserDto userDto = UserDto.builder()
                .username(authentication.getName())
                .build();

        AuthResponseDto authResponse = authService.checkAuthStatus(userDto, accessToken);
        return ResponseEntity.ok(authResponse);
    }

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
    public ResponseEntity<TokensResponseDto> refreshToken(@Valid @RequestHeader(HttpHeaders.AUTHORIZATION) RefreshTokenRequestDto refreshToken) {
        return ResponseEntity.ok(authService.refreshToken(refreshToken));
    }

//    @GetMapping("/private")
//    @PreAuthorize("hasRole('USER')")
//    public ResponseEntity<Map<String, Object>> testingPrivateRoute(
//            Authentication authentication,
//            HttpServletRequest request) {
//
//        UserDto userDto = UserDto.builder()
//                .username(authentication.getName())
//                .build();
//
//        AuthResponseDto authResponse = authService.checkAuthStatus(userDto);
//
//        Map<String, Object> response = Map.of(
//                "ok", true,
//                "message", "Hola Mundo Private",
//                "user", authResponse.user(),
//                "userEmail", authentication.getName()
//        );
//
//        return ResponseEntity.ok(response);
//    }

//    @GetMapping("/private2")
//    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_USER')")
//    public ResponseEntity<Map<String, Object>> privateRoute2(Authentication authentication) {
//        UserDto userDto = UserDto.builder()
//                .username(authentication.getName())
//                .build();
//
//        AuthResponseDto authResponse = authService.checkAuthStatus(userDto);
//
//        return ResponseEntity.ok(Map.of(
//                "ok", true,
//                "user", authResponse.user()
//        ));
//    }
}
