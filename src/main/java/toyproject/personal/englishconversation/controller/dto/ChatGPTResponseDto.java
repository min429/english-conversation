package toyproject.personal.englishconversation.controller.dto;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ChatGPTResponseDto {
    private List<Choice> choices;

    @Getter
    public static class Choice { // 내부 클래스는 static 타입이어야 매핑 대상이 됨
        private Integer index;
        private Message message;
    }

}
