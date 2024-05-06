package toyproject.personal.englishconversation.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import toyproject.personal.englishconversation.controller.dto.chat.ChatRequestDto;
import toyproject.personal.englishconversation.domain.Chat;
import toyproject.personal.englishconversation.domain.message.GPTMessage;
import toyproject.personal.englishconversation.service.ChatService;

import java.io.IOException;
import java.util.List;

@Slf4j
@Controller
@RequestMapping("/chat")
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;

    @PostMapping
    public ResponseEntity<GPTMessage> chat(@RequestBody ChatRequestDto request) throws IOException {
        log.debug("request: {}", request);
        return ResponseEntity.ok(chatService.chat(request));
    }

    @PostMapping("/all")
    public ResponseEntity<List<Chat>> readAll(@RequestBody Long userId) {
        return ResponseEntity.ok(chatService.findChatsAllById(userId));
    }
}
