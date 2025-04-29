package x10.drivemate.domain.chat.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import x10.drivemate.common.exception.GeneralException;
import x10.drivemate.common.status.ErrorStatus;
import x10.drivemate.domain.chat.dto.ChatResponseDto;
import x10.drivemate.domain.chat.dto.GptResponseDto;

@Service
@RequiredArgsConstructor
public class ChatGptService {
    @Value("${openai.api.key}")
    private String apiKey;

    @Value("${openai.api.url}")
    private String apiUrl;

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    public GptResponseDto.GptSummaryKeywordDto generateSummaryAndKeywords(String chatLog) {
        String prompt =
                "Please summarize the following conversation in one sentence and extract key keywords. Provide the output in the following format:\n" +
                "\n" +
                "Summary: {your summary}\n" +
                "Keywords: {keyword1, keyword2, keyword3}\n" +
                "\n" +
                "Conversation:\n" +
                chatLog;

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + apiKey);
        headers.setContentType(MediaType.APPLICATION_JSON);

        String body = "{"
                + "\"model\": \"text-davinci-003\","
                + "\"prompt\": \"" + prompt + "\","
                + "\"max_tokens\": 150"
                + "}";

        HttpEntity<String> request = new HttpEntity<>(body, headers);

        ResponseEntity<String> response = restTemplate.exchange(apiUrl, HttpMethod.POST, request, String.class);

        try {
            JsonNode responseJson = objectMapper.readTree(response.getBody());
            String getResponse = responseJson.get("choices").get(0).get("test").asText().trim();

            String[] parts = getResponse.split("Keywords:");
            String summary = parts[0].trim();
            String keywords = parts.length > 1 ? parts[1].trim() : "";

            return GptResponseDto.GptSummaryKeywordDto.builder()
                    .summary(summary)
                    .keywords(keywords)
                    .build();

        } catch (Exception e) {
            throw new GeneralException(ErrorStatus.CHAT_NOT_FOUND);
        }

    }
}
