package x10.drivemate.domain.member.controller;

import x10.drivemate.common.response.ApiResponse;
import x10.drivemate.common.status.SuccessStatus;
import x10.drivemate.domain.member.dto.MemberRequestDto;
import x10.drivemate.domain.member.dto.MemberResponseDto;
import x10.drivemate.domain.member.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/members")
@RequiredArgsConstructor
public class MemberRestContoller {

    private final MemberService memberService;

    // 기본 회원가입
    @PostMapping("/signup")
    public ResponseEntity<ApiResponse> signUp(
            @RequestBody @Valid MemberRequestDto.signupDto request
    ) {
        MemberResponseDto.signupResultdto response = memberService.signupMember(request);
        return ApiResponse.onSuccess(SuccessStatus._OK, response);
    }

    // 카카오 소셜 로그인


    // 개인정보 업데이트
    @PostMapping("")
    public ResponseEntity<ApiResponse> userInfo(
            @RequestBody @Valid MemberRequestDto.userInfoDto request
    ) {
        MemberResponseDto.userInfodto response = memberService.userInfo(request);
        return ApiResponse.onSuccess(SuccessStatus._OK, response);
    }

    // 개인정보 조회
    @GetMapping("/info/{memberId}")
    public ResponseEntity<ApiResponse> getUserinfo(
            @PathVariable Long memberId
    ) {
        MemberResponseDto.userInfodto response = memberService.getUserInfo(memberId);
        return ApiResponse.onSuccess(SuccessStatus._OK, response);
    }
}
