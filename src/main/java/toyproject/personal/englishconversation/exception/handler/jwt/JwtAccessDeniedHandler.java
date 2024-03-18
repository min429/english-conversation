package toyproject.personal.englishconversation.exception.handler.jwt;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import java.io.IOException;

/**
 * 스프링 시큐리티 필터에서 인가 실패 시 예외 처리
 */
@Slf4j
public class JwtAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException e) throws IOException, ServletException {
        log.error("[AccessDeniedException] ex", e);
        response.sendError(HttpServletResponse.SC_FORBIDDEN, "접근 권한 없음");
    }
}
