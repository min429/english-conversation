package toyproject.personal.englishconversation.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import toyproject.personal.englishconversation.controller.dto.ChatGPTRequestDto;
import toyproject.personal.englishconversation.domain.message.GPTMessage;
import toyproject.personal.englishconversation.service.ChatService;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = ChatController.class)
class ChatControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ChatService chatService;

    private Map<String, String> getCorrectMap() {
        Map<String, String> correctMap = new HashMap<>();
        correctMap.put("wrong sentence", "correct sentence");
        return correctMap;
    }

    @Test
    void chat() throws Exception {
        // Given
        GPTMessage gptMessage = GPTMessage.builder()
                .id("1")
                .correctMap(getCorrectMap())
                .explanation("explanation")
                .message("message")
                .build();
        given(chatService.processChatRequest(any(ChatGPTRequestDto.class))).willReturn(gptMessage);

        // WhenThen
        mockMvc.perform(post("/chat")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("1"))
                .andExpect(jsonPath("$.explanation").value("explanation"))
                .andExpect(jsonPath("$.message").value("message"))
                .andExpect(jsonPath("$.correctMap['wrong sentence']").value("correct sentence"));
    }

}