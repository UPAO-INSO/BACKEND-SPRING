package upao.inso.dclassic.config;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import upao.inso.dclassic.auth.model.TokenModel;
import upao.inso.dclassic.auth.repository.ITokenRepository;
import upao.inso.dclassic.filter.JwtAuthenticationFilter;

@Configuration
@EnableWebSecurity // Enable Spring Security
@RequiredArgsConstructor
@EnableMethodSecurity
public class SecurityConfig {
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final AuthenticationProvider authProvider;
    private final ITokenRepository tokenRepository;

    @Bean
    public SecurityFilterChain authenticationSecurityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authRequest ->
                        authRequest
                                .requestMatchers("/auth/**").permitAll()
                                .requestMatchers("/swagger-ui/**").permitAll()
                                .requestMatchers("/docs/swagger-config").permitAll()
                                .requestMatchers("/docs/**").permitAll()
                                .anyRequest().authenticated()
                )
                .sessionManagement(sessionManagement ->
                        sessionManagement
                                .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .authenticationProvider(authProvider)
                .logout(logout -> {
                    logout.logoutUrl("/auth/logout")
                            .addLogoutHandler((request, response, authentication) -> {
                                String token = request.getHeader(HttpHeaders.AUTHORIZATION);
                                logout(token);
                            })
                            .logoutSuccessHandler((request, response, authentication) -> {
                                response.setStatus(HttpServletResponse.SC_OK);
                                response.getWriter().write("Logout successful");
                                SecurityContextHolder.clearContext();
                            })
                            .logoutSuccessUrl("/auth/login");
                })
                .build();
    }

    private void logout(final String token) {
        if (token == null || !token.startsWith("Bearer ")) {
            throw new IllegalArgumentException("Invalid token");
        }

        final String jwtToken = token.substring(7);
        final TokenModel existToken = tokenRepository.findByToken(jwtToken)
                .orElseThrow(() -> new IllegalArgumentException("Token not found"));

        existToken.setExpired(true);
        existToken.setRevoked(true);
        tokenRepository.save(existToken);
    }
}
