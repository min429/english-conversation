package toyproject.personal.englishconversation.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;
import toyproject.personal.englishconversation.SecurityTestConfig;
import toyproject.personal.englishconversation.UserConfig;
import toyproject.personal.englishconversation.config.jwt.JwtFactory;
import toyproject.personal.englishconversation.config.jwt.JwtProperties;
import toyproject.personal.englishconversation.controller.dto.jwt.JwtRefreshRequestDto;
import toyproject.personal.englishconversation.domain.User;
import toyproject.personal.englishconversation.mapper.RefreshTokenMapper;
import toyproject.personal.englishconversation.mapper.UserMapper;

import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@Transactional
@SpringBootTest
@AutoConfigureMockMvc
@Import({SecurityTestConfig.class, UserConfig.class})
class JwtControllerTest {

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    JwtProperties jwtProperties;

    @Autowired
    UserMapper userMapper;

    @Autowired
    RefreshTokenMapper refreshTokenMapper;

    @BeforeEach
    public void mockMvcSetUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(context)
                .build();
        userMapper.deleteAll();
    }

    @DisplayName("createNewAccessToken: 새로운 액세스 토큰을 발급한다.")
    @Test
    public void createNewAccessToken() throws Exception {
        // given
        final String url = "/token/create";

        userMapper.save(User.builder()
                .email("user@gmail.com")
                .password("test")
                .nickname("nickname")
                .build());
        User user = userMapper.findByEmail("user@gmail.com");

        String refreshToken = JwtFactory.builder()
                .claims(Map.of("id", user.getId()))
                .build()
                .createToken(jwtProperties);

        refreshTokenMapper.save(user.getId(), refreshToken);

        JwtRefreshRequestDto request = new JwtRefreshRequestDto(refreshToken);
        final String requestBody = objectMapper.writeValueAsString(request);

        // when
        ResultActions resultActions = mockMvc.perform(post(url)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(requestBody));

        // then
        resultActions
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.accessToken").isNotEmpty());
    }
}
