package toyproject.personal.englishconversation.exception;

public class UserPasswordException extends RuntimeException {
    public UserPasswordException(String message) {
        super(message);
    }

    public UserPasswordException(String message, Throwable cause) {
        super(message, cause);
    }
}
