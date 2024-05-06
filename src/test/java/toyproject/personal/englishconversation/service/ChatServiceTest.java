package toyproject.personal.englishconversation.service;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import toyproject.personal.englishconversation.config.chatgpt.ChatGPTContent;
import toyproject.personal.englishconversation.controller.dto.chat.ChatGPTRequestDto;
import toyproject.personal.englishconversation.controller.dto.chat.ChatRequestDto;
import toyproject.personal.englishconversation.controller.dto.chat.Message;
import org.springframework.context.annotation.Import;
import toyproject.personal.englishconversation.NoSecurityTestConfig;
import toyproject.personal.englishconversation.domain.message.GPTMessage;
import toyproject.personal.englishconversation.repository.ChatCustomRedisRepository;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@SpringBootTest
@Import(NoSecurityTestConfig.class)
class ChatServiceTest {

    @Mock
    private ChatGPTService chatGPTService;

    @Mock
    private ChatCustomRedisRepository chatCustomRedisRepository;

    @InjectMocks
    private ChatService chatService;

    private List<Message> getMessages(){
        List<Message> messages = new LinkedList<>();
        messages.add(new Message("user", "message"));
        return messages;
    }

    private Map<String, String> getCorrectMap() {
        Map<String, String> correctMap = new HashMap<>();
        correctMap.put("wrong sentence", "correct sentence");
        return correctMap;
    }

    private ChatRequestDto getChatRequestDto() {
        ChatRequestDto chatRequestDto = ChatRequestDto.builder()
                .userId(1L)
                .topic("topic")
                .messages(getMessages())
                .build();

        return chatRequestDto;
    }

    private GPTMessage getGPTMessage() {
        return GPTMessage.builder()
                .correctMap(getCorrectMap())
                .explanation("explanation")
                .message("message")
                .build();
    }

    @Test
    void chat() throws IOException {
        // Given
        GPTMessage gptMessage = getGPTMessage();
        ChatRequestDto chatRequestDto = getChatRequestDto();
        given(chatGPTService.callChatGPTApi(any(ChatGPTRequestDto.class), any(ChatRequestDto.class))).willReturn(getGPTMessage());

        // When
        GPTMessage result = chatService.chat(chatRequestDto);

        // Then
        assertThat(gptMessage).isEqualTo(result);
    }
}
