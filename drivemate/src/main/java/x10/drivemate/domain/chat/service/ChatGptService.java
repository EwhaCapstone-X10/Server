package x10.drivemate.domain.chat.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import x10.drivemate.common.exception.GeneralException;
import x10.drivemate.common.status.ErrorStatus;
import x10.drivemate.domain.chat.dto.ChatResponseDto;
import x10.drivemate.domain.chat.dto.GptResponseDto;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
                "Please summarize the following conversation in one sentence and extract key keywords. Provide the summary and keywords **in Korean**. The summary must be only one sentence.\n" +
                "\n" +
                "Summary: {your summary}\n" +
                "Keywords: {keyword1, keyword2, keyword3}\n" +
                "\n" +
                "Conversation:\n" +
                chatLog;

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + apiKey);
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("model", "gpt-4");
        requestBody.put("max_tokens", 150);

        List<Map<String, String>> messages = new ArrayList<>();
        messages.add(createMessage("system", "You are a helpful assistant that summarizes conversations and extracts keywords."));
        messages.add(createMessage("user", prompt));

        requestBody.put("messages", messages);


        String jsonBody;
        try {
            jsonBody = objectMapper.writeValueAsString(requestBody);
        } catch (Exception e) {
            throw new GeneralException(ErrorStatus.AI_BODY_ERROR);
        }

        HttpEntity<String> request = new HttpEntity<>(jsonBody, headers);

        ResponseEntity<String> response = restTemplate.exchange(apiUrl, HttpMethod.POST, request, String.class);

        try {
            JsonNode responseJson = objectMapper.readTree(response.getBody());
            JsonNode firstChoice = responseJson.get("choices").get(0);
            JsonNode messageNode = firstChoice.get("message");

            if (messageNode == null) {
                throw new GeneralException(ErrorStatus.EXTERNAL_API_ERROR);
            }

            JsonNode contentNode = messageNode.get("content");
            if (contentNode == null) {
                throw new GeneralException(ErrorStatus.EXTERNAL_API_ERROR);
            }

            String getResponse = contentNode.asText().trim();

            String[] parts = getResponse.split("Keywords:", 2);
            String summary = parts[0].trim();
            String keywords = parts.length > 1 ? parts[1].trim() : "";

            if (summary.startsWith("Summary:")) {
                summary = summary.substring("Summary:".length()).trim();
            }

            return GptResponseDto.GptSummaryKeywordDto.builder()
                    .summary(summary)
                    .keywords(keywords)
                    .build();

        } catch (Exception e) {
            throw new GeneralException(ErrorStatus.CHATGPT_PARSING_ERROR);
        }

    }

    private Map<String, String> createMessage(String role, String content) {
        Map<String, String> message = new HashMap<>();
        message.put("role", role);
        message.put("content", content);
        return message;
    }
}
