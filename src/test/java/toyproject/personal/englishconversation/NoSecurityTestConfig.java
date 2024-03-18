package toyproject.personal.englishconversation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.web.SecurityFilterChain;
import toyproject.personal.englishconversation.mapper.UserMapper;
import toyproject.personal.englishconversation.service.JwtService;
import toyproject.personal.englishconversation.service.UserService;

/**
 * 인증/인가에 대한 테스트를 하지 않을 시 import하는 클래스
 */
@TestConfiguration
public class NoSecurityTestConfig {

    @MockBean
    private SecurityFilterChain securityFilterChain;
    @MockBean
    private UserMapper userMapper;
    @MockBean
    private UserService userService;
    @MockBean
    private JwtService jwtService;
}
