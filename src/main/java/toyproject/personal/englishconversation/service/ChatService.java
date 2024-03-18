package toyproject.personal.englishconversation.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import toyproject.personal.englishconversation.controller.dto.chatgpt.ChatGPTRequestDto;
import toyproject.personal.englishconversation.domain.message.GPTMessage;

import java.io.IOException;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChatService {

    private final ChatGPTService chatGPTService;

    public GPTMessage processChatRequest(ChatGPTRequestDto requestDto) throws IOException {
        return chatGPTService.callChatGPTApi(requestDto);
    }
}
