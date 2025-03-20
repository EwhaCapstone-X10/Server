package x10.drivemate.global.security;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CustomUserPrincipal {
    private Long memberId;
    private String email;
    private String kakaoId;
}
