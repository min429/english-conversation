package toyproject.personal.englishconversation.config.jwt;

import io.jsonwebtoken.Jwts;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import toyproject.personal.englishconversation.SecurityTestConfig;
import toyproject.personal.englishconversation.UserConfig;
import toyproject.personal.englishconversation.domain.User;
import toyproject.personal.englishconversation.exception.TokenValidationException;
import toyproject.personal.englishconversation.mapper.UserMapper;

import java.time.Duration;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@Transactional
@SpringBootTest
@Import({SecurityTestConfig.class, UserConfig.class})
class JwtProviderTest {

    @Autowired
    private JwtProvider jwtProvider;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private JwtProperties jwtProperties;

    @DisplayName("generateToken(): 유저 정보와 만료 기간을 전달해 토큰을 만들 수 있다.")
    @Test
    void generateToken() {
        // given
        userMapper.save(User.builder()
                .email("test@gmail.com")
                .password("1234")
                .build());
        User user = userMapper.findByEmail("test@gmail.com");

        // when
        String token = jwtProvider.generateToken(user, Duration.ofMinutes(30), List.of("ROLE_USER"));
        token = jwtProvider.getAccessToken(token);

        // then
        Long userId = Jwts.parser()
                .setSigningKey(jwtProperties.getSecretKey())
                .parseClaimsJws(token)
                .getBody()
                .get("id", Long.class);

        assertThat(userId).isEqualTo(user.getId());
    }

    @DisplayName("isValidToken(): 만료된 토큰인 경우에 유효성 검증에 실패한다.")
    @Test
    void validToken_invalidToken() {
        // given
        String token = JwtFactory.builder()
                .expiration(new Date(new Date().getTime() - Duration.ofMinutes(30).toMillis()))
                .build()
                .createToken(jwtProperties);

        // when then
        assertThatThrownBy(() -> jwtProvider.ValidateToken(token))
                .isInstanceOf(TokenValidationException.class);
    }


    @DisplayName("isValidToken(): 유효한 토큰인 경우에 유효성 검증에 성공한다.")
    @Test
    void isValidToken_ValidToken() {
        // given
        String token = JwtFactory.withDefaultValues()
                .createToken(jwtProperties);

        // when then
        assertDoesNotThrow(() -> jwtProvider.ValidateToken(token));
    }


    @DisplayName("getAuthentication(): 토큰 기반으로 인증정보를 가져올 수 있다.")
    @Test
    void getAuthentication() {
        // given
        String userEmail = "user@email.com";
        String token = JwtFactory.builder()
                .subject(userEmail)
                .build()
                .createToken(jwtProperties);

        // when
        Authentication authentication = jwtProvider.getAuthentication(token);

        // then
        assertThat(authentication.getPrincipal()).isEqualTo(userEmail); // getPrincipal: 사용자 정보
    }

    @DisplayName("getUserId(): 토큰으로 유저 ID를 가져올 수 있다.")
    @Test
    void getUserId() {
        // given
        Long userId = 1L;
        String token = JwtFactory.builder()
                .claims(Map.of("id", userId))
                .build()
                .createToken(jwtProperties);

        // when
        Long userIdByToken = jwtProvider.getUserId(token);

        // then
        assertThat(userIdByToken).isEqualTo(userId);
    }
}