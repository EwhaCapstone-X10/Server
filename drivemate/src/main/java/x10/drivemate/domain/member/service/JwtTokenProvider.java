package x10.drivemate.domain.member.service;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Base64;
import java.util.Date;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;


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
        // 기존 비밀 키가 Base64로 인코딩된 문자열일 경우, 이를 복호화하여 Key를 생성합니다.
        if (secretKey != null && !secretKey.isEmpty()) {
            byte[] decodedKey = Base64.getDecoder().decode(secretKey);
            signingKey = Keys.hmacShaKeyFor(decodedKey);  // Base64 복호화한 비밀 키로 키 객체 생성
        } else {
            // 비밀 키가 없거나 잘못된 경우, 안전한 256비트 키 생성
            signingKey = Keys.secretKeyFor(SignatureAlgorithm.HS256);  // HS256 알고리즘에 맞는 안전한 키 생성
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
        return Jwts.builder()
                .setSubject(kakaoId)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + validity))
                .signWith(Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8)), SignatureAlgorithm.HS256)
                .compact();
    }

    // JWT 토큰에서 사용자 ID(kakaoId) 추출
    public String getUserPk(String token) {
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().getSubject();
    }

    // 유효성 검사
    public boolean validateToken(String token) {
        try {
            Jws<Claims> claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
            return !claims.getBody().getExpiration().before(new Date());
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            log.info("잘못된 JWT 서명입니다.");
        } catch (ExpiredJwtException e) {
            log.info("만료된 JWT 토큰입니다.");
        } catch (UnsupportedJwtException e) {
            log.info("지원되지 않는 JWT 토큰입니다.");
        } catch (IllegalArgumentException e) {
            log.info("JWT 토큰이 잘못되었습니다.");
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
