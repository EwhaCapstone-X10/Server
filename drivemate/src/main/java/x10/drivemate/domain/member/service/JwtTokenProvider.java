package x10.drivemate.domain.member.service;

import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Base64;
import java.util.Date;
import io.jsonwebtoken.*;

import javax.crypto.spec.SecretKeySpec;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtTokenProvider {
    private final long ACCESS_TOKEN_VALIDITY = 1000L * 60 * 60; // 1시간
    private final long REFRESH_TOKEN_VALIDITY = 1000L * 60 * 60 * 24 * 7; // 7일

    @Value("${jwt.secretKey}")
    private String secretKey;

    private Key signingKey;  // 사용할 Key를 안전한 비밀 키로 설정할 변수

    @PostConstruct
    protected void init() {
        if (secretKey != null && !secretKey.isEmpty()) {
            byte[] decodedKey = Base64.getDecoder().decode(secretKey);  // Base64로 디코딩된 비밀 키
            signingKey = new SecretKeySpec(decodedKey, "HmacSHA256");  // 서명 키 생성
        } else {
            signingKey = Keys.secretKeyFor(SignatureAlgorithm.HS256);  // 기본 서명 키 사용
        }
    }


    public String createToken(String kakaoId) {
        return createToken(kakaoId, ACCESS_TOKEN_VALIDITY);
    }

    public String createRefreshToken(String kakaoId) {
        return createToken(kakaoId, REFRESH_TOKEN_VALIDITY);
    }

    private String createToken(String kakaoId, long validity) {
        Date now = new Date();

        // JWT 토큰 생성
        String token = Jwts.builder()
                .setSubject(kakaoId)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + validity))
                .signWith(signingKey, SignatureAlgorithm.HS256)  // HS256 알고리즘으로 서명
                .compact();

        log.info("Generated JWT Token: {}", token);  // 생성된 토큰 로깅
        return token;
    }

    // JWT 토큰에서 사용자 ID(kakaoId) 추출
    public String getUserPk(String token) {
        return Jwts.parser()
                .setSigningKey(signingKey)  // signingKey 사용
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public boolean validateToken(String token) {
        try {
            Jws<Claims> claims = Jwts.parser()
                    .setSigningKey(signingKey)
                    .parseClaimsJws(token);
            return !claims.getBody().getExpiration().before(new Date());
        } catch (JwtException | IllegalArgumentException e) {
            log.error("Invalid or expired token: {}", token, e);
        }
        return false;
    }

    // 엑세스 토큰 재발급
    public String refreshAccessToken(String refreshToken) {
        if (validateToken(refreshToken)) {
            String kakaoId = getUserPk(refreshToken);
            return createToken(kakaoId);
        }
        return null;  // 유효하지 않은 refresh토큰에 대해서는 null 반환
    }

}
