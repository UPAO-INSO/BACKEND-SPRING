package team.upao.dev.auth.service.impl;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import team.upao.dev.auth.dto.AuthResponseDto;
import team.upao.dev.auth.dto.LoginDto;
import team.upao.dev.auth.dto.RefreshTokenRequestDto;
import team.upao.dev.auth.dto.SignupDto;
import team.upao.dev.auth.enums.TokenType;
import team.upao.dev.auth.model.TokenModel;
import team.upao.dev.auth.repository.ITokenRepository;
import team.upao.dev.auth.service.AuthService;
import team.upao.dev.auth.jwt.JwtService;
import team.upao.dev.employees.dto.EmployeeDto;
import team.upao.dev.employees.services.EmployeeService;
import team.upao.dev.jobs.model.JobModel;
import team.upao.dev.jobs.service.impl.JobServiceImpl;
import team.upao.dev.users.enums.UserRole;
import team.upao.dev.users.model.UserModel;
import team.upao.dev.users.service.UserService;

import java.time.Instant;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final UserService userService;
    private final EmployeeService employeeService;
    private final JobServiceImpl jobService;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final ITokenRepository tokenRepository;
    private final AuthenticationManager authenticationManager;

    @Override
    public AuthResponseDto login(final LoginDto request) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getUsername(),
                            request.getPassword()
                    )
            );

            UserModel userModel = this.userService.findModelByUsername(request.getUsername());

            SecurityContextHolder.getContext().setAuthentication(authentication);
            userModel.setLastLogin(Instant.now());
            AuthResponseDto tokens = jwtService.generateToken(authentication);

            revokeAllUserTokens(userModel);
            saveUserToken(userModel, tokens.accessToken());

            return tokens;
        } catch (RuntimeException e) {
            log.error("Error during login: {}", e.getMessage());
            throw new IllegalArgumentException("Invalid username or password");
        }
    }

    @Transactional
    @Override
    public void register(final SignupDto request) {
        if(this.userService.existsByUsername(request.getUsername())) {
            throw new IllegalArgumentException("Username already exists");
        }

        if(this.userService.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Email already exists");
        }

        UserModel user = UserModel.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .email(request.getEmail())
                .role(UserRole.USER)
                .isActive(true)
                .build();

        this.userService.create(user);

        log.info("Creating user: {}", user.toString());

        JobModel job = jobService.findByTitle(request.getJobTitle());
        Double salary = jobService.getSalaryByJobTitle(job);

        EmployeeDto employeeDto = EmployeeDto.builder()
                .name(request.getUsername())
                .lastname(request.getLastname())
                .phone(request.getPhone())
                .salary(salary)
                .jobId(job.getId())
                .userId(user.getId())
                .build();

        this.employeeService.create(employeeDto);
    }

    private void saveUserToken(UserModel user, String jwtToken) {
        TokenModel token = TokenModel.builder()
                .user(user)
                .token(jwtToken)
                .tokenType(TokenType.BEARER)
                .expired(false)
                .revoked(false)
                .build();
        tokenRepository.save(token);
    }

    private void revokeAllUserTokens(UserModel user) {
        final List<TokenModel> tokens = tokenRepository.findAllByUser(user);
        if(tokens.isEmpty()) {
            return;
        }
        tokens.forEach(token -> {
            token.setExpired(true);
            token.setRevoked(true);
        });
        tokenRepository.saveAll(tokens);
    }

    public AuthResponseDto refreshToken(@Valid RefreshTokenRequestDto token) {
        String refreshToken = token.getRefreshToken();

        if(!jwtService.isRefreshToken(refreshToken)) {
            throw new IllegalArgumentException("Invalid refresh token");
        }

        String username = jwtService.extractUsername(refreshToken);
        UserModel user = userService.findModelByUsername(username);

        UsernamePasswordAuthenticationToken auth =
                new UsernamePasswordAuthenticationToken(
                        user,
                        null,
                        user.getAuthorities());

        String accessToken = jwtService.generateAccessToken(auth);
        return new AuthResponseDto(accessToken, refreshToken);
    }
}
