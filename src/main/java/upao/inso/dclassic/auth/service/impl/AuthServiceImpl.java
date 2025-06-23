package upao.inso.dclassic.auth.service.impl;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import upao.inso.dclassic.auth.dto.AuthResponseDto;
import upao.inso.dclassic.auth.dto.LoginDto;
import upao.inso.dclassic.auth.dto.RefreshTokenRequestDto;
import upao.inso.dclassic.auth.dto.SignupDto;
import upao.inso.dclassic.auth.enums.TokenType;
import upao.inso.dclassic.auth.model.TokenModel;
import upao.inso.dclassic.auth.repository.ITokenRepository;
import upao.inso.dclassic.auth.service.AuthService;
import upao.inso.dclassic.auth.jwt.JwtService;
import upao.inso.dclassic.employees.model.EmployeeModel;
import upao.inso.dclassic.employees.services.EmployeeService;
import upao.inso.dclassic.jobs.model.JobModel;
import upao.inso.dclassic.jobs.service.impl.JobServiceImpl;
import upao.inso.dclassic.users.enums.UserRole;
import upao.inso.dclassic.users.model.UserModel;
import upao.inso.dclassic.users.service.UserService;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

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

    public AuthResponseDto login(final LoginDto request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );

        UserModel user = this.userService.findByUsername(request.getUsername());

        SecurityContextHolder.getContext().setAuthentication(authentication);
        user.setLastLogin(Instant.now());
        AuthResponseDto tokens = jwtService.generateToken(authentication);
        revokeAllUserTokens(user);
        saveUserToken(user, tokens.accessToken());
        return tokens;
    }

    public void register(final SignupDto request) {
        log.info("Registering user: {}", request.getJobTitle());

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
                .phone(request.getPhone())
                .role(UserRole.USER)
                .build();

        JobModel job = jobService.findByTitle(request.getJobTitle());
        log.info("Job found: {}", job.getTitle());
        Double salary = jobService.getSalaryByJobTitle(job);

        EmployeeModel employee = EmployeeModel.builder()
                .name(request.getUsername())
                .lastname(request.getLastname())
                .phone(request.getPhone())
                .salary(salary)
                .job(job)
                .user(user)
                .build();

        this.employeeService.create(employee);
        this.userService.create(user);
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
        UserModel user = this.userService.findByUsername(username);

        UsernamePasswordAuthenticationToken auth =
                new UsernamePasswordAuthenticationToken(
                        user,
                        null,
                        user.getAuthorities());

        String accessToken = jwtService.generateAccessToken(auth);
        return new AuthResponseDto(accessToken, refreshToken);
    }
}
