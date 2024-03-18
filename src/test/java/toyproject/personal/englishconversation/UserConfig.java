package toyproject.personal.englishconversation;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import toyproject.personal.englishconversation.domain.User;
import toyproject.personal.englishconversation.mapper.UserMapper;

@TestConfiguration
public abstract class UserConfig {
    @Autowired
    private UserMapper userMapper;
    @BeforeEach
    void setUp() {
        userMapper.save(User.builder()
                .email("abc@gmail.com")
                .password("123")
                .nickname("test1")
                .build());
        userMapper.save(User.builder()
                .email("def@gmail.com")
                .password("456")
                .nickname("test2")
                .build());
    }
}
