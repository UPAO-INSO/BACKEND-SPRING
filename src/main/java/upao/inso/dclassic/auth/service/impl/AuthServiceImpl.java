package upao.inso.dclassic.auth.service.impl;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import upao.inso.dclassic.auth.dto.AuthResponseDto;
import upao.inso.dclassic.auth.dto.LoginDto;
import upao.inso.dclassic.auth.dto.RefreshTokenRequestDto;
import upao.inso.dclassic.auth.dto.SignupDto;
import upao.inso.dclassic.auth.enums.TokenType;
import upao.inso.dclassic.auth.model.TokenModel;
import upao.inso.dclassic.auth.repository.ITokenRepository;
import upao.inso.dclassic.auth.service.AuthService;
import upao.inso.dclassic.auth.jwt.JwtService;
import upao.inso.dclassic.employees.dto.EmployeeDto;
import upao.inso.dclassic.employees.services.EmployeeService;
import upao.inso.dclassic.jobs.model.JobModel;
import upao.inso.dclassic.jobs.service.impl.JobServiceImpl;
import upao.inso.dclassic.users.dto.UserDto;
import upao.inso.dclassic.users.enums.UserRole;
import upao.inso.dclassic.users.mapper.UserMapper;
import upao.inso.dclassic.users.model.UserModel;
import upao.inso.dclassic.users.repository.IUserRepository;
import upao.inso.dclassic.users.service.UserService;

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
