package toyproject.personal.englishconversation.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import toyproject.personal.englishconversation.controller.dto.ChatGPTRequestDto;
import toyproject.personal.englishconversation.domain.message.GPTMessage;
import toyproject.personal.englishconversation.service.ChatService;

import java.io.IOException;

@Slf4j
@Controller
@RequestMapping("/chat")
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;

    @PostMapping
    public ResponseEntity<GPTMessage> chat(@RequestBody ChatGPTRequestDto request) throws IOException {
        log.debug("request: {}", request);
        return ResponseEntity.ok(chatService.processChatRequest(request));
    }

}
