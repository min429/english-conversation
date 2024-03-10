package toyproject.personal.englishconversation.controller.dto.chatgpt;

import lombok.*;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Message {
    private String role;
    private String content;
}
