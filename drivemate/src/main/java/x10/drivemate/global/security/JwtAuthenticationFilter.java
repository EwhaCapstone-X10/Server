package x10.drivemate.global.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import x10.drivemate.common.exception.GeneralException;
import x10.drivemate.common.status.ErrorStatus;
import x10.drivemate.domain.member.entity.Member;
import x10.drivemate.domain.member.repository.MemberRepository;
import x10.drivemate.domain.member.service.JwtTokenProvider;
import x10.drivemate.domain.member.service.KakaoService;

import java.io.IOException;
import java.util.ArrayList;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;
    private final MemberRepository memberRepository;

    public JwtAuthenticationFilter(JwtTokenProvider jwtTokenProvider, MemberRepository memberRepository) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.memberRepository = memberRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = extractTokenFromHeader(request);

        // JWT 토큰 검증 (카카오 액세스 토큰은 제외)
        if (token != null && jwtTokenProvider.validateToken(token)) {
            String kakaoId = jwtTokenProvider.getUserPk(token);  // JWT에서 kakaoId 추출
            Member member = memberRepository.findByKakaoId(kakaoId).orElseThrow(() -> new GeneralException(ErrorStatus.MEMBER_NOT_FOUND));
            CustomUserPrincipal userPrincipal = CustomUserPrincipal.builder().memberId(member.getMemberId()).kakaoId(kakaoId).build();
            Authentication authentication = new UsernamePasswordAuthenticationToken(userPrincipal, null, new ArrayList<>());
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        filterChain.doFilter(request, response);
    }

    private String extractTokenFromHeader(HttpServletRequest request) {
        String bearerToken = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}
