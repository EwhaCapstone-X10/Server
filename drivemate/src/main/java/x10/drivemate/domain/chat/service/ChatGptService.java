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
                "다음 대화를 한 문장으로 요약하고, 핵심 키워드를 추출하세요. " +
                        "이건 챗봇 대화 서비스를 이용하는 고객에게 대화 내역과 함께 제공되는 요약과 키워드를 위함입니다."+
                        "고객 본인에게 제공되는 것이므로 user 주어(고객, 상대방)는 생략해도 됩니다."+
                        "요약은 20자 이내로, 문장은 반드시 '음'이나 '함' 과 같은 줄임 어미로 끝나야 합니다. " +
                        "키워드는 **한국어로** 제공해주세요.\n" +
                "\n" +
                        "요약: {your summary}\n" +
                        "키워드: {keyword1, keyword2, keyword3}\n" +
                "\n" +
                "대화 내용:\n" +
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

            String[] parts = getResponse.split("키워드:", 2);
            String summary = parts[0].trim();
            String keywords = parts.length > 1 ? parts[1].trim() : "";

            if (summary.startsWith("요약:")) {
                summary = summary.substring("요약:".length()).trim();
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
