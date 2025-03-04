package x10.drivemate.domain.member.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class KakaoUserInfoDto {
    private String kakaoId;
    private String nickname;
    private String profileImageUrl;
}
