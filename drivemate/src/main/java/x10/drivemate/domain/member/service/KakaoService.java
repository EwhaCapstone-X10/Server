package x10.drivemate.domain.member.service;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import x10.drivemate.common.exception.GeneralException;
import x10.drivemate.common.status.ErrorStatus;
import x10.drivemate.domain.member.entity.LoginStatus;
import x10.drivemate.domain.member.entity.Member;
import x10.drivemate.domain.member.repository.MemberRepository;

@Service
@RequiredArgsConstructor
public class KakaoService {
    private static final String KAKAO_USERINFO_URL = "https://kapi.kakao.com/v2/user/me";
    private final MemberRepository memberRepository;
    private final RestTemplate restTemplate;

    // 카카오 액세스 토큰으로 카카오 ID 가져오기
    public String getKakaoIdFromAccessToken(String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        try {
            ResponseEntity<JsonNode> response = restTemplate.exchange(KAKAO_USERINFO_URL, HttpMethod.GET, entity, JsonNode.class);
            if (response.getStatusCode() == HttpStatus.OK) {
                return response.getBody().path("id").asText();  // 카카오 ID 추출
            } else {
                throw new GeneralException(ErrorStatus.EXPIRED_TOKEN);  // 유효하지 않은 토큰
            }
        } catch (HttpClientErrorException e) {
            if (e.getStatusCode() == HttpStatus.UNAUTHORIZED) {
                throw new GeneralException(ErrorStatus.EXPIRED_TOKEN);  // 만료된 액세스 토큰 처리
            }
            throw new GeneralException(ErrorStatus.KAKAO_API_ERROR, e.getMessage());
        }
    }

    // 회원 정보 가져오기 (기존 회원이 없다면 생성)
    public Member getOrCreateMember(String kakaoId) {
        return memberRepository.findByKakaoId(kakaoId)
                .orElseGet(() -> createNewMember(kakaoId));
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
