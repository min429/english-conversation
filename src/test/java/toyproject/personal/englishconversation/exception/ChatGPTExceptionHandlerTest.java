package toyproject.personal.englishconversation.exception;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.web.client.HttpClientErrorException;
import toyproject.personal.englishconversation.controller.dto.chatgpt.ChatGPTRequestDto;
import toyproject.personal.englishconversation.service.ChatService;

import java.io.IOException;
import java.util.stream.Stream;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class ChatGPTExceptionHandlerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ChatService chatService;


    /** ChatGPT API 응답 상태코드에 따른 테스트 **/
    @ParameterizedTest
    @MethodSource("httpStatusProvider")
    public void HttpClientErrorExceptionHandle(HttpStatus status, String expectedCode, String expectedMessage) throws Exception {
        // Given
        HttpClientErrorException exception = new HttpClientErrorException(status, expectedMessage);
        Mockito.when(chatService.processChatRequest(any(ChatGPTRequestDto.class)))
                .thenThrow(exception);

        // When
        ResultActions resultActions = mockMvc.perform(post("/chat")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"));

        // Then
        resultActions
                .andExpect(status().is(status.value()))
                .andExpect(jsonPath("$.code").value(expectedCode))
                .andExpect(jsonPath("$.message").value(expectedMessage));
    }

    /** ChatGPT API 응답 상태 코드 종류 **/
    static Stream<Arguments> httpStatusProvider() {
        return Stream.of(
                Arguments.of(HttpStatus.UNAUTHORIZED, "EX", "서버 오류"),
                Arguments.of(HttpStatus.TOO_MANY_REQUESTS, "EX", "요청 속도 초과"),
                Arguments.of(HttpStatus.INTERNAL_SERVER_ERROR, "EX", "ChatGPT 서버 오류"),
                Arguments.of(HttpStatus.SERVICE_UNAVAILABLE, "EX", "ChatGPT 서버 오류")
        );
    }

    @Test
    public void objectMapperExceptionHandle() throws Exception {
        // Given
        given(chatService.processChatRequest(any(ChatGPTRequestDto.class)))
                .willThrow(new IOException());

        // When
        ResultActions resultActions =  mockMvc.perform(post("/chat")
                .contentType(MediaType.APPLICATION_JSON)
                .content("")); // 잘못된 Json 형식 -> Json 매핑 실패

        // Then
        resultActions
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.code").value("EX"))
                .andExpect(jsonPath("$.message").value("서버 오류"));
    }

}
