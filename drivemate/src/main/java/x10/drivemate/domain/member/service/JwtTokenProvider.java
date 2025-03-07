package x10.drivemate.domain.member.service;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Date;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;

@Component
@RequiredArgsConstructor
public class JwtTokenProvider {
    private String AUTHORITIES_KEY = "auth"; // 보안키
    private String BEARER_TYPE = "Bearer ";
    private final long ACCESS_TOKEN_VALIDITY = 1000L * 60 * 60; // 1시간
    private final long REFRESH_TOKEN_VALIDITY = 1000L * 60 * 60 * 24 * 7; // 7일

    @Value("${jwt.secretKey}")
    private String secretKey;

    @PostConstruct
    protected void init() {
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes(StandardCharsets.UTF_8));
    }

    public String createToken(String kakaoId) {
        return createToken(kakaoId, ACCESS_TOKEN_VALIDITY);
    }

    public String createRefreshToken(String kakaoId) {
        return createToken(kakaoId, REFRESH_TOKEN_VALIDITY);
    }

    private String createToken(String kakaoId, long validity) {
        Date now = new Date();
        return Jwts.builder()
                .setSubject(kakaoId)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + validity))
                .signWith(Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8)), SignatureAlgorithm.HS256)
                .compact();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
