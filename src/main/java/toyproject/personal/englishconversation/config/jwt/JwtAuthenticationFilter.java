package toyproject.personal.englishconversation.config.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import toyproject.personal.englishconversation.exception.handler.jwt.JwtAuthenticationEntryPoint;

import java.io.IOException;

/**
 * Jwt 인증 요청이 왔을 때 로그인 검증 및 Jwt 발급을 처리하는 필터
 */
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtProvider jwtProvider;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final static String HEADER_AUTHORIZATION = "Authorization";

    /** 스프링 시큐리티 필터에서 인증 처리
     * 이미 CustomAuthenticationEntryPoint에서 response.sendError를 통해
     * 에러 발생 여부를 저장해 놔서 별도의 예외처리를 할 필요가 없음
     * **/
    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain)  throws ServletException, IOException {

        // "/user/signup", "/user/login", "/token/**" 경로는 인증과 상관없이 허용
        if (request.getRequestURI().startsWith("/user/signup")
                || request.getRequestURI().startsWith("/user/login")
                || request.getRequestURI().startsWith("/token/")) {
            filterChain.doFilter(request, response);
            return;
        }

        String authorizationHeader = request.getHeader(HEADER_AUTHORIZATION);
        String accessToken = jwtProvider.getAccessToken(authorizationHeader); // 헤더에서 액세스 토큰 가져옴

        try {
            jwtProvider.ValidateToken(accessToken);
        } catch (AuthenticationException e) {
            jwtAuthenticationEntryPoint.commence(request, response, e);
            return;
        }

        // 사용자 인증 정보 - Principal(신원 ex. 사용자 이름), Credentials(자격 증명 ex. 비밀번호), Authorities(권한)
        Authentication authentication = jwtProvider.getAuthentication(accessToken);
        // SecurityContext에 사용자 인증 정보 저장 -> 언제든 애플리케이션의 전역에서 접근 가능
        SecurityContextHolder.getContext().setAuthentication(authentication);

        filterChain.doFilter(request, response);
    }
}
