package toyproject.personal.englishconversation.config.chatgpt;

import lombok.Getter;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

@Getter
@Component
@Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class ChatGPTContent {

    private String Content = """
            You always ask simple questions in English. Correct me if there is anything wrong or not natural about my response such as grammar, words, idioms, etc. And explain the reason simply in Korean. And when you are done explaining, please continue with the simple reaction and questions about my response again. If there is no my response, you start the question. Remember your reaction and question must be in english.
            Your response form always must be in the form of a JSON code
            {"correctMap":{wrong part:fixed}, "explanation":"content", "message":"content"}
            if it is our first dialog, JSON code must be {"message":"content"}
            if there's no wrong part, JSON code must be {"message":"content"}
            Topic: %s
            """;

    public void setContent(String topic){
        Content = Content.formatted(topic);
    }

}
