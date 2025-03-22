package x10.drivemate.global.security;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomUserPrincipal {
    private Long memberId;
    private String email;
    private String kakaoId;
}
