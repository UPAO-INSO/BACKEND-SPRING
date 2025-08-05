package upao.inso.dclassic.auth.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import upao.inso.dclassic.auth.dto.AuthResponseDto;
import upao.inso.dclassic.users.model.UserModel;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;

@Service
@Slf4j
public class JwtService {
    @Value("${jwt.secret.key}")
    private String secretKey;

    @Value("${jwt.expiration.time}")
    private Long jwtExpiration;

    @Value("${jwt.refresh.token.expiration.time}")
    private Long jwtRefreshExpiration;

    public AuthResponseDto generateToken(Authentication auth) {
        String accessToken = generateAccessToken(auth);
        String refreshToken = generateRefreshToken(auth);

        return new AuthResponseDto(accessToken, refreshToken);
    }

    public String getUsernameFromToken(String token) {
        return getClaim(token, Claims::getSubject);
    }

    public String generateAccessToken(Authentication auth) {
        return generateToken(auth, jwtExpiration, new HashMap<>());
    }

    public String generateRefreshToken(Authentication auth) {
        Map<String, String> claims = new HashMap<>();
        claims.put("tokenType", "refresh");
        return generateToken(auth, jwtRefreshExpiration, claims);
    }

    private String generateToken(Authentication authentication, Long expirationInMs, Map<String, String> claims) {
        UserDetails userPrincipal = (UserDetails) authentication.getPrincipal();

        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + expirationInMs);

        return Jwts.builder()
                .header()
                .add("typ", "JWT")
                .and()
                .subject(userPrincipal.getUsername())
                .claims(claims)
                .issuedAt(now)
                .expiration(expiryDate)
                .signWith(getSignInKey())
                .compact();
    }

    private SecretKey getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public boolean isRefreshToken(String token) {
        Claims claims = extractAllClaims(token);
        if (claims == null) {
            return false;
        }
        return "refresh".equals(claims.get("tokenType"));
    }

    public boolean isValidToken(String token) {
        return extractAllClaims(token) != null;
    }

    public boolean validateTokenForUser(String token, UserDetails userDetails) {
        final String username = extractUsernameFromToken(token);
        return username != null
                && username.equals(userDetails.getUsername());
    }

    public String extractUsernameFromToken(String token) {
        Claims claims = extractAllClaims(token);

        if (claims != null) {
            return claims.getSubject();
        }
        return null;
    }

    private Claims extractAllClaims(String token) {
        Claims claims;

        claims = Jwts.parser()
                .verifyWith(getSignInKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();

        return claims;
    }

    public <T> T getClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Date getExpiration(String token) {
        return getClaim(token, Claims::getExpiration);
    }

    private boolean isTokenExpired(String token) {
        return !getExpiration(token).before(new Date());
    }

    public String extractUsername(String token) {
        return getClaim(token, Claims::getSubject);
    }

    private String getToken(Map<String, Object> extraClaims, UserModel user) {
        extraClaims.put("username", user.getUsername());
        return Jwts
                .builder()
                .claims(extraClaims)
                .subject(user.getId().toString())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + jwtExpiration))
                .signWith(getSignInKey())
                .compact();
    }

    public boolean isRefreshTokenValid(String token, UserDetails user) {
        return isRefreshToken(token) && isTokenExpired(token) && getUsernameFromToken(token).equals(user.getUsername());
    }

}

