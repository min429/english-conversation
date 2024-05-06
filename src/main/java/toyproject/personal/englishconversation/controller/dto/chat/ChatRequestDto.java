package toyproject.personal.englishconversation.controller.dto.chat;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.LinkedList;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ChatRequestDto {
    private Long userId;
    private String topic;
    @JsonDeserialize(as = LinkedList.class) // LinkedList로 매핑하도록 지정 (Jackson default: ArrayList)
    private List<Message> messages; // 이전 채팅 내용 포함

    @Builder
    public ChatRequestDto(Long userId, String topic, List<Message> messages) {
        this.userId = userId;
        this.topic = topic;
        this.messages = messages;
    }
}
