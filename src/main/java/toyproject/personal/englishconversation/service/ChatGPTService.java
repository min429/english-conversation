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
import toyproject.personal.englishconversation.config.ChatGPTConfig;
import toyproject.personal.englishconversation.config.ChatGPTContent;
import toyproject.personal.englishconversation.controller.dto.ChatGPTRequestDto;
import toyproject.personal.englishconversation.controller.dto.ChatGPTResponseDto;
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

    public GPTMessage callChatGPTApi(ChatGPTRequestDto requestDto) throws IOException {
        HttpEntity<String> entity = prepareRequestEntity(requestDto);
        ResponseEntity<String> response = executeApiRequest(entity);

        return processResponse(response);
    }

    /** API 요청 준비 **/
    private HttpEntity<String> prepareRequestEntity(ChatGPTRequestDto requestDto) throws JsonProcessingException {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + chatGPTConfig.getApiKey());

        setRequestDto(requestDto);

        return new HttpEntity<>(objectMapper.writeValueAsString(requestDto), headers);
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
    private void setRequestDto(ChatGPTRequestDto requestDto) {
        chatGPTContent.setContent(requestDto.getTopic());
        requestDto.addContentToMessages(chatGPTContent.getContent());
    }

}
