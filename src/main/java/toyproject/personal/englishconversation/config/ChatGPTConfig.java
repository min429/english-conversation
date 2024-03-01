package toyproject.personal.englishconversation.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class ChatGPTConfig {

    @Value("${GPT_API_KEY}")
    private String API_KEY;
    public static final String URL = "https://api.openai.com/v1/chat/completions";

    @Bean
    public RestTemplate restTemplate(){
        return new RestTemplate();
    }

    public String getApiKey(){
        return API_KEY;
    }

}
