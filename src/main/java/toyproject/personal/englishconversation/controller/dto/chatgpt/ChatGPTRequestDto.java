package toyproject.personal.englishconversation.controller.dto.chatgpt;

import com.fasterxml.jackson.annotation.JsonProperty;
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
    private String model;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY) // 역직렬화할 때만 포함하도록 함 (api 요청할 때는 필요 없음)
    private String topic;

    /** ChatGPTContent 추가 (메시지 세팅) **/
    public void addContentToMessages(String content) {
        ((LinkedList<Message>) messages).addFirst(new Message("user", content));
    }

    @Builder
    private ChatGPTRequestDto(List<Message> messages, String model, String topic) {
        this.messages = messages;
        this.model = model;
        this.topic = topic;
    }
}