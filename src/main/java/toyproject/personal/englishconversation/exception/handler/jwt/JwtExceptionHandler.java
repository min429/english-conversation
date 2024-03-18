package toyproject.personal.englishconversation.exception.handler.jwt;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import toyproject.personal.englishconversation.exception.ErrorResult;
import toyproject.personal.englishconversation.exception.TokenValidationException;

/**
 * 컨트롤러에서 토큰 검증 실패 시 예외처리
 */
@Slf4j
@RestControllerAdvice
public class JwtExceptionHandler {

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler
    public ErrorResult handleTokenValidationException(TokenValidationException e) {
        log.error("[TokenValidationException] ex", e);
        return new ErrorResult("EX", e.getMessage());
    }
}
