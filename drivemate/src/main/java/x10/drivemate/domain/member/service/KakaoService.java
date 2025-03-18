package x10.drivemate.domain.member.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class KakaoService {
    private static final String KAKAO_USERINFO_URL = "https://kapi.kakao.com/v2/user/me";
    private final JwtTokenProvider jwtTokenProvider;

    public String getUserInfo(String accessToken) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);

        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<String> response = restTemplate.exchange(KAKAO_USERINFO_URL, HttpMethod.GET, entity, String.class);

        // 카카오 응답에서 사용자 ID 추출
        String kakaoId = extractKakaoIdFromResponse(response.getBody());
        String jwtToken = jwtTokenProvider.createToken(kakaoId);

        return jwtToken;
    }

    private String extractKakaoIdFromResponse(String response) {
        int idStart = response.indexOf("\"id\":") + 5;
        int idEnd = response.indexOf(",", idStart);
        return response.substring(idStart, idEnd);
    }
}
