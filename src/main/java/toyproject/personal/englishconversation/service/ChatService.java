package toyproject.personal.englishconversation.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import toyproject.personal.englishconversation.controller.dto.chat.ChatGPTRequestDto;
import toyproject.personal.englishconversation.controller.dto.chat.ChatRequestDto;
import toyproject.personal.englishconversation.domain.Chat;
import toyproject.personal.englishconversation.domain.message.GPTMessage;
import toyproject.personal.englishconversation.domain.message.UserMessage;
import toyproject.personal.englishconversation.repository.ChatCustomRedisRepository;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ChatService {

    private final ChatGPTService chatGPTService;
    private final ChatCustomRedisRepository chatCustomRedisRepository;

    public GPTMessage chat(ChatRequestDto chatRequestDto) throws IOException {
        ChatGPTRequestDto chatGPTRequestDto = ChatGPTRequestDto.builder()
                .messages(chatRequestDto.getMessages())
                .build();

        GPTMessage gptMessage = chatGPTService.callChatGPTApi(chatGPTRequestDto, chatRequestDto);
        save(chatRequestDto.getUserId(), new UserMessage(chatGPTRequestDto.getLastMessage()), gptMessage);

        return gptMessage;
    }

    public void save(Long userId, UserMessage userMessage, GPTMessage gptMessage) throws JsonProcessingException {
        Long messageId = chatCustomRedisRepository.generateMessageId(userId); // 자동 증가

        String key = userId + ":" + messageId;
        LocalDateTime now = LocalDateTime.now();
        Chat chat = new Chat(key, messageId, userId, userMessage, gptMessage, now);

        chatCustomRedisRepository.save(chat);
    }

    public Chat findChatbyId(String id) {
        return chatCustomRedisRepository.findChatById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 채팅 메시지"));
    }

    public List<Chat> findChatsAllById(Long userId) {
        return chatCustomRedisRepository.findAllChatsByUserId(userId);
    }
}
