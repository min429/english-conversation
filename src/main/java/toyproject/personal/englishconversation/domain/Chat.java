package toyproject.personal.englishconversation.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import toyproject.personal.englishconversation.domain.message.GPTMessage;
import toyproject.personal.englishconversation.domain.message.UserMessage;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@RedisHash("chat")
public class Chat {

    @Id
    private String key;

    private Long messageId;
    private Long userId;

    private UserMessage userMessage;
    private GPTMessage gptMessage;
    private LocalDateTime timestamp;

    @Builder
    public Chat(String key, Long messageId, Long userId, UserMessage userMessage, GPTMessage gptMessage, LocalDateTime timestamp) {
        this.key = key;
        this.messageId = messageId;
        this.userId = userId;
        this.userMessage = userMessage;
        this.gptMessage = gptMessage;
        this.timestamp = timestamp;
    }
}
