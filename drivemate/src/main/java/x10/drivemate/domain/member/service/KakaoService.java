package x10.drivemate.domain.member.service;

import com.fasterxml.jackson.databind.JsonNode;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import x10.drivemate.domain.member.entity.LoginStatus;
import x10.drivemate.domain.member.entity.Member;
import x10.drivemate.domain.member.repository.MemberRepository;

@Service
@RequiredArgsConstructor
public class KakaoService {
    private static final String KAKAO_USERINFO_URL = "https://kapi.kakao.com/v2/user/me";
    private final JwtTokenProvider jwtTokenProvider;
    private final MemberRepository memberRepository;
    private final RestTemplate restTemplate;

    public String getUserInfo(HttpServletRequest request) {
        String accessToken = extractAccessTokenFromHeader(request);

        // 카카오 액세스 토큰을 이용해 사용자 정보 요청
        String kakaoId = getKakaoIdFromAccessToken(accessToken);

        // 기존 회원이 있는지 확인
        Member member = memberRepository.findByKakaoId(kakaoId)
                .orElseGet(() -> createNewMember(kakaoId));

        // JWT 토큰 생성 후 반환
        return jwtTokenProvider.createToken(member.getKakaoId());
    }

    // Authorization 헤더에서 Bearer 토큰 추출
    private String extractAccessTokenFromHeader(HttpServletRequest request) {
        String authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            return authorizationHeader.substring(7); // "Bearer " 이후의 토큰 추출
        }
        throw new IllegalArgumentException("Access token is missing or invalid.");
    }

    // 카카오 액세스 토큰을 사용하여 카카오 ID 가져오기
    public String getKakaoIdFromAccessToken(String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);
        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<JsonNode> response = restTemplate.exchange(KAKAO_USERINFO_URL, HttpMethod.GET, entity, JsonNode.class);

        // 카카오 응답에서 사용자 ID 추출
        return extractKakaoIdFromResponse(response.getBody());
    }

    // 카카오 응답에서 ID 추출
    private String extractKakaoIdFromResponse(JsonNode response) {
        return response.path("id").asText();
    }

    // 새로운 회원을 생성하는 메서드
    private Member createNewMember(String kakaoId) {
        Member newMember = Member.builder()
                .kakaoId(kakaoId)
                .nickname("defaultNickname")
                .loginStatus(LoginStatus.unfinished)
                .build();
        return memberRepository.save(newMember);
    }
}