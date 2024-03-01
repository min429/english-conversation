package toyproject.personal.englishconversation.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;
import toyproject.personal.englishconversation.controller.ChatController;

import java.io.IOException;

@Slf4j
@RestControllerAdvice(assignableTypes = ChatController.class)
public class ChatGPTExceptionHandler {

    @ExceptionHandler
    public ResponseEntity<ErrorResult> HttpClientErrorExceptionHandle(HttpClientErrorException e) {
        log.error("[HttpClientErrorExceptionHandle] ex", e);

        ErrorResult errorResult = new ErrorResult("EX", "서버 오류");

        if(e.getStatusCode() == HttpStatus.UNAUTHORIZED)
            errorResult = new ErrorResult("EX", "서버 오류");

        if(e.getStatusCode() == HttpStatus.TOO_MANY_REQUESTS)
            errorResult = new ErrorResult("EX", "요청 속도 초과");

        if(e.getStatusCode() == HttpStatus.INTERNAL_SERVER_ERROR)
            errorResult = new ErrorResult("EX", "ChatGPT 서버 오류");

        /** 추후 자동으로 재요청하는 로직 구현 예정 **/
        if(e.getStatusCode() == HttpStatus.SERVICE_UNAVAILABLE)
            errorResult = new ErrorResult("EX", "ChatGPT 서버 오류");

        return new ResponseEntity<>(errorResult, HttpStatus.valueOf(e.getRawStatusCode()));
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler
    public ErrorResult objectMapperExceptionHandle(IOException e){
        log.error("[objectMapperExceptionHandle] ex", e);
        return new ErrorResult("EX", "서버 오류");
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler
    public ErrorResult internalExceptionHandle(Exception e){
      log.error("[internalExceptionHandle] ex", e);
      return new ErrorResult("EX", "서버 오류");
    }
}
