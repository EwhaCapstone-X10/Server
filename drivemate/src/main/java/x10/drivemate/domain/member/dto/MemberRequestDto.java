package x10.drivemate.domain.member.dto;

import x10.drivemate.domain.member.entity.MemberSex;
import x10.drivemate.domain.member.entity.Mode;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

public class MemberRequestDto {
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class signupDto {
        @NotBlank(message = "이메일은 필수입니다.")
        private String email;

        @NotNull(message = "비밀번호는 필수입니다.")
        private String password;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class userInfoDto {
        private String name;
        private LocalDate birthdate;
        private MemberSex sex;
        private Mode mode;
        private String occupation;
        private List<String> interests;
    }
}
