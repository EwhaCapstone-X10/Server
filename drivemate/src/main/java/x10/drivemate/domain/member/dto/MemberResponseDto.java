package x10.drivemate.domain.member.dto;

import x10.drivemate.domain.member.entity.LoginStatus;
import x10.drivemate.domain.member.entity.MemberSex;
import x10.drivemate.domain.member.entity.Mode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class MemberResponseDto {

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class signupResultdto {
        private Long memberId;
        private LocalDateTime signupTime;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class kakaoLoginResultdto {
        private Long memberId;
        private String kakaoId;
        private String jwtToken;
        private LoginStatus loginStatus;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class userInfodto {
        private Long memberId;
        private String name;
        private LocalDate birthdate;
        private MemberSex sex;
        private Mode mode;
        private String occupation;
        private List<String> interests;
        private LoginStatus loginStatus;
    }
}
