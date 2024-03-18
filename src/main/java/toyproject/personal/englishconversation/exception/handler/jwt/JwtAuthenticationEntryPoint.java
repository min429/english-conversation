package toyproject.personal.englishconversation.exception.handler.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import toyproject.personal.englishconversation.exception.ErrorResult;

import java.io.IOException;
import java.io.PrintWriter;

/**
 * 스프링 시큐리티 필터에서 인증 실패 시 예외 처리
 */
@Slf4j
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException e) throws IOException, ServletException {
        log.error("[AuthenticationException] ex", e);

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json;charset=UTF-8");
        ErrorResult errorResult = new ErrorResult("EX", "인증 실패");
        String json = objectMapper.writeValueAsString(errorResult);

        PrintWriter writer = response.getWriter();
        writer.print(json);
        writer.flush();
    }
}
