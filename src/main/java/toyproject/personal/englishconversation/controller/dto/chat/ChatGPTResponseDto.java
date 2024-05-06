package toyproject.personal.englishconversation.controller.dto.chat;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ChatGPTResponseDto {
    private List<Choice> choices;

    @Getter
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class Choice { // 내부 클래스는 static 타입이어야 매핑 대상이 됨
        private Integer index;
        private Message message;
    }

    @Builder
    private ChatGPTResponseDto(Integer index, Message message) {
        Choice choice = new Choice(index, message);
        this.choices = new ArrayList<>();
        this.choices.add(choice);
    }

}
