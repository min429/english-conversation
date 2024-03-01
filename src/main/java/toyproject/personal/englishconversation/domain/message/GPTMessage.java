package toyproject.personal.englishconversation.domain.message;


import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Map;

@Getter
@Builder // 일부 필드만 초기화 가능
@EqualsAndHashCode // equals()를 필드 비교 방식으로 구현
public class GPTMessage {
    private String id;
    private Map<String, String> correctMap;
    private String explanation;
    private String message;
}
