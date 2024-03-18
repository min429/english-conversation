package toyproject.personal.englishconversation.service;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import toyproject.personal.englishconversation.config.chatgpt.ChatGPTContent;
import toyproject.personal.englishconversation.controller.dto.chatgpt.ChatGPTRequestDto;
import toyproject.personal.englishconversation.controller.dto.chatgpt.Message;
import org.springframework.context.annotation.Import;
import toyproject.personal.englishconversation.NoSecurityTestConfig;
import toyproject.personal.englishconversation.domain.message.GPTMessage;

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

    private ChatGPTRequestDto getChatGPTRequestDto() {
        ChatGPTRequestDto requestDto = ChatGPTRequestDto.builder()
                .messages(getMessages())
                .model("gpt4")
                .topic("topic")
                .build();

        // ChatGPTContent (기본설정) 추가
        ChatGPTContent chatGPTContent = new ChatGPTContent();
        chatGPTContent.setContent(requestDto.getTopic());
        requestDto.addContentToMessages(chatGPTContent.getContent());

        return requestDto;
    }

    private GPTMessage getGPTMessage() {
        return GPTMessage.builder()
                .id("1")
                .correctMap(getCorrectMap())
                .explanation("explanation")
                .message("message")
                .build();
    }

    @Test
    void processChatRequest() throws IOException {
        // Given
        GPTMessage gptMessage = getGPTMessage();
        ChatGPTRequestDto chatGPTRequestDto = getChatGPTRequestDto();
        given(chatGPTService.callChatGPTApi(chatGPTRequestDto)).willReturn(getGPTMessage());

        // When
        GPTMessage result = chatService.processChatRequest(chatGPTRequestDto);

        // Then
        assertThat(gptMessage).isEqualTo(result);
        verify(chatGPTService).callChatGPTApi(chatGPTRequestDto);
    }
}
