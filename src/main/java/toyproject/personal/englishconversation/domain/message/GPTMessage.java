package toyproject.personal.englishconversation.domain.message;


import lombok.*;

import java.util.Map;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@EqualsAndHashCode // equals()를 필드 비교 방식으로 구현x
public class GPTMessage {
    private Map<String, String> correctMap;
    private String explanation;
    private String message;

    @Builder // 일부 필드만 초기화 가능
    public GPTMessage(Map<String, String> correctMap, String explanation, String message) {
        this.correctMap = correctMap;
        this.explanation = explanation;
        this.message = message;
    }
}
