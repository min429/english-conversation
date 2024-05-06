package toyproject.personal.englishconversation.controller.dto.chat;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.LinkedList;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ChatGPTRequestDto {

    /** from client **/
    @JsonDeserialize(as = LinkedList.class) // LinkedList로 매핑하도록 지정 (Jackson default: ArrayList)
    private List<Message> messages; // 이전 채팅 내용 포함
    private final String model = "gpt-4";

    /** ChatGPTContent 추가 (메시지 세팅) **/
    public void addContentToMessages(String content) {
        ((LinkedList<Message>) messages).addFirst(new Message("user", content));
    }

    @Builder
    private ChatGPTRequestDto(List<Message> messages) {
        this.messages = messages;
    }

    @JsonIgnore
    public String getLastMessage() {
        return messages.get(messages.size() - 1).getContent();
    }
}