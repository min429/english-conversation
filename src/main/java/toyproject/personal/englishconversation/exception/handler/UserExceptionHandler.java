package toyproject.personal.englishconversation.exception.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import toyproject.personal.englishconversation.controller.UserController;
import toyproject.personal.englishconversation.exception.ErrorResult;
import toyproject.personal.englishconversation.exception.UserEmailException;
import toyproject.personal.englishconversation.exception.UserPasswordException;

@Slf4j
@RestControllerAdvice(assignableTypes = UserController.class)
public class UserExceptionHandler {
    @ExceptionHandler
    public ErrorResult handleUserEmailException(UserEmailException e) {
        log.error("[UserEmailException] ex", e);
        return new ErrorResult("EX", e.getMessage());
    }

    @ExceptionHandler
    public ErrorResult handleUserPasswordException(UserPasswordException e) {
        log.error("[UserPasswordException] ex", e);
        return new ErrorResult("EX", e.getMessage());
    }
}
