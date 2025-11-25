package team.upao.dev.config;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import team.upao.dev.auth.model.TokenModel;
import team.upao.dev.auth.repository.ITokenRepository;
import team.upao.dev.filter.JwtAuthenticationFilter;

import java.util.List;

@Configuration
@EnableWebSecurity // Enable Spring Security
@RequiredArgsConstructor
@EnableMethodSecurity
public class SecurityConfig {
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final AuthenticationProvider authProvider;
    private final ITokenRepository tokenRepository;
    private final CustomAccessDeniedHandler customAccessDeniedHandler;

    @Bean
    public SecurityFilterChain authenticationSecurityFilterChain(HttpSecurity http) throws Exception {
        return http
                .cors(Customizer.withDefaults())
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authRequest ->
                        authRequest
                                .requestMatchers("/ws/**").permitAll()
                                .requestMatchers("/auth/check-status").authenticated()
                                .requestMatchers("/auth/private/**").authenticated()
                                .requestMatchers("/auth/login", "/auth/register", "/auth/refresh-token").permitAll()
                                .requestMatchers("/swagger-ui/**").permitAll()
                                .requestMatchers("/docs/swagger-config").permitAll()
                                .requestMatchers("/docs/**").permitAll()
                                .requestMatchers("payments/culqi/webhook").permitAll()
                                .anyRequest().authenticated()
                )
                .exceptionHandling(exception ->
                        exception.accessDeniedHandler(customAccessDeniedHandler)
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

    @Bean
    public CorsConfigurationSource corsConfigurationSource(){
        CorsConfiguration configuration= new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("http://localhost:4200", "https://punto-de-sal.netlify.app"));
        configuration.setAllowedMethods(List.of("GET","POST","PUT","DELETE", "PATCH", "OPTIONS"));
        configuration.setAllowCredentials(true);
        configuration.setAllowedHeaders(List.of(
                "Authorization",
                "Content-Type",
                "ngrok-skip-browser-warning",
                "Access-Control-Allow-Origin",
                "Access-Control-Allow-Credentials",
                "Access-Control-Allow-Headers",
                "Access-Control-Request-Headers",
                "Access-Control-Request-Method"
        ));
        UrlBasedCorsConfigurationSource source= new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**",configuration);
        return source;
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
