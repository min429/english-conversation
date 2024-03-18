package toyproject.personal.englishconversation.controller.dto.user;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SignUpRequestDto {
    private String email;
    private String password;
    private String nickname;
}
