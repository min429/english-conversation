package toyproject.personal.englishconversation.controller.dto.jwt;

import lombok.*;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class JwtRefreshRequestDto {
    private String refreshToken;
}
