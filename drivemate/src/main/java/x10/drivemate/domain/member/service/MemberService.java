package x10.drivemate.domain.member.service;

import org.springframework.http.ResponseEntity;
import x10.drivemate.common.response.ApiResponse;
import x10.drivemate.domain.member.dto.MemberRequestDto;
import x10.drivemate.domain.member.dto.MemberResponseDto;
import jakarta.validation.Valid;
import x10.drivemate.global.security.CustomUserPrincipal;

public interface MemberService {
    MemberResponseDto.signupResultdto signupMember(MemberRequestDto.signupDto request);
    MemberResponseDto.userInfodto userInfo(CustomUserPrincipal userPrincipal, MemberRequestDto.userInfoDto request);
    MemberResponseDto.userInfodto getUserInfo(Long memberId);
    MemberResponseDto.kakaoLoginResultdto handleLogin(String kakaoId);
}
