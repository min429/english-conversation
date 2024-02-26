package toyproject.personal.englishconversation.domain.message;


import lombok.Getter;

import java.util.Map;

@Getter
public class GPTMessage {
    private String id;
    private Map<String, String> correctMap;
    private String explanation;
    private String message;
}
