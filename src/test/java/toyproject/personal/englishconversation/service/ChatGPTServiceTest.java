package toyproject.personal.englishconversation.service;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.client.RestTemplate;
import toyproject.personal.englishconversation.config.chatgpt.ChatGPTConfig;
import toyproject.personal.englishconversation.config.chatgpt.ChatGPTContent;
import toyproject.personal.englishconversation.controller.dto.chat.ChatGPTRequestDto;
import toyproject.personal.englishconversation.controller.dto.chat.ChatGPTResponseDto;
import toyproject.personal.englishconversation.controller.dto.chat.ChatRequestDto;
import toyproject.personal.englishconversation.controller.dto.chat.Message;
import toyproject.personal.englishconversation.NoSecurityTestConfig;
import toyproject.personal.englishconversation.domain.message.GPTMessage;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@SpringBootTest
@Import(NoSecurityTestConfig.class)
class ChatGPTServiceTest {

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private ChatGPTConfig chatGPTConfig;

    @Mock
    private ChatGPTContent chatGPTContent;

    @InjectMocks
    private ChatGPTService chatGPTService;

    private Message getMessage(){
        return new Message("assistant", "message");
    }

    private List<Message> getMessages(){
        List<Message> messages = new LinkedList<>();
        messages.add(new Message("user", "message"));
        return messages;
    }

    private ChatGPTRequestDto getChatGPTRequestDto(ChatRequestDto chatRequestDto) {
        ChatGPTRequestDto requestDto = ChatGPTRequestDto.builder()
                .messages(chatRequestDto.getMessages())
                .build();

        // ChatGPTContent (기본설정) 추가
        ChatGPTContent chatGPTContent = new ChatGPTContent();
        chatGPTContent.setContent(chatRequestDto.getTopic());
        requestDto.addContentToMessages(chatGPTContent.getContent());

        return requestDto;
    }

    private ChatRequestDto getChatRequestDto() {
        ChatRequestDto chatRequestDto = ChatRequestDto.builder()
                .userId(1L)
                .topic("topic")
                .messages(getMessages())
                .build();

        return chatRequestDto;
    }

    private ChatGPTResponseDto getChatGPTResponseDto(){
        return ChatGPTResponseDto.builder()
                .index(1)
                .message(getMessage())
                .build();
    }

    private Map<String, String> getCorrectMap() {
        Map<String, String> correctMap = new HashMap<>();
        correctMap.put("wrong sentence", "correct sentence");
        return correctMap;
    }

    private GPTMessage getGPTMessage() {
        return GPTMessage.builder()
                .correctMap(getCorrectMap())
                .explanation("explanation")
                .message("message")
                .build();
    }

    @Test
    public void callChatGPTApiTest() throws Exception {
        // Given
        GPTMessage gptMessage = getGPTMessage();
        ChatRequestDto chatRequestDto = getChatRequestDto();
        ChatGPTRequestDto chatGPTRequestDto = getChatGPTRequestDto(chatRequestDto);
        given(chatGPTConfig.getApiKey()).willReturn("test_api_key");
        given(chatGPTContent.getContent()).willReturn("test content");
        given(restTemplate.postForEntity(any(String.class), any(HttpEntity.class), any(Class.class)))
                .willReturn(new ResponseEntity<>("responseBody", HttpStatus.OK));
        given(objectMapper.readValue(any(String.class), eq(ChatGPTResponseDto.class)))
                .willReturn(getChatGPTResponseDto());
        given(objectMapper.readValue(any(String.class), eq(GPTMessage.class)))
                .willReturn(getGPTMessage());
        given(objectMapper.writeValueAsString(any(ChatGPTRequestDto.class))).willReturn("expected json");

        // When
        GPTMessage result = chatGPTService.callChatGPTApi(chatGPTRequestDto, chatRequestDto);

        // Then
        assertThat(gptMessage).isEqualTo(result);
    }
}
