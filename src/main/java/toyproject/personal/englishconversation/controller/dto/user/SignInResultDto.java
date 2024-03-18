package toyproject.personal.englishconversation.controller.dto.user;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SignInResultDto {
    private String accessToken;
    private String refreshToken;
}
