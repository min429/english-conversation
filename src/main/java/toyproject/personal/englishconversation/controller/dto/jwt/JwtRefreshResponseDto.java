package toyproject.personal.englishconversation.controller.dto.jwt;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class JwtRefreshResponseDto {
    private String accessToken;
    private String refreshToken;
}

