package toyproject.personal.englishconversation.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import toyproject.personal.englishconversation.config.chatgpt.ChatGPTConfig;
import toyproject.personal.englishconversation.config.chatgpt.ChatGPTContent;
import toyproject.personal.englishconversation.controller.dto.chat.ChatGPTRequestDto;
import toyproject.personal.englishconversation.controller.dto.chat.ChatGPTResponseDto;
import toyproject.personal.englishconversation.controller.dto.chat.ChatRequestDto;
import toyproject.personal.englishconversation.domain.message.GPTMessage;

import java.io.IOException;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChatGPTService {

    private final ObjectMapper objectMapper;
    private final RestTemplate restTemplate;
    private final ChatGPTConfig chatGPTConfig;
    private final ChatGPTContent chatGPTContent; // 요청 스코프 빈

    public GPTMessage callChatGPTApi(ChatGPTRequestDto chatGPTRequestDto, ChatRequestDto chatRequestDto) throws IOException {
        HttpEntity<String> entity = prepareRequestEntity(chatGPTRequestDto, chatRequestDto);
        ResponseEntity<String> response = executeApiRequest(entity);

        return processResponse(response);
    }

    /** API 요청 준비 **/
    private HttpEntity<String> prepareRequestEntity(ChatGPTRequestDto chatGPTRequestDto, ChatRequestDto chatRequestDto) throws JsonProcessingException {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + chatGPTConfig.getApiKey());

        setRequestDto(chatGPTRequestDto, chatRequestDto);

        return new HttpEntity<>(objectMapper.writeValueAsString(chatGPTRequestDto), headers);
    }

    /** API 요청 **/
    private ResponseEntity<String> executeApiRequest(HttpEntity<String> entity) {
        return restTemplate.postForEntity(ChatGPTConfig.URL, entity, String.class);
    }

    /** GPT 응답 처리 **/
    private GPTMessage processResponse(ResponseEntity<String> response) throws JsonProcessingException {
        ChatGPTResponseDto responseDto = objectMapper.readValue(response.getBody(), ChatGPTResponseDto.class);
        String contentJson = responseDto.getChoices().get(0).getMessage().getContent();

        return objectMapper.readValue(contentJson, GPTMessage.class);
    }

    /** API 요청 전 메시지 세팅 **/
    private void setRequestDto(ChatGPTRequestDto chatGPTRequestDto, ChatRequestDto chatRequestDto) {
        chatGPTContent.setContent(chatRequestDto.getTopic());
        chatGPTRequestDto.addContentToMessages(chatGPTContent.getContent());
    }
}
