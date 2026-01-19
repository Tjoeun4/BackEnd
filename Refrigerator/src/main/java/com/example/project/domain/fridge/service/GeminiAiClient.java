package com.example.project.domain.fridge.service;

import java.time.Duration;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class GeminiAiClient implements ItemAiClient {

    private final ObjectMapper objectMapper;

    @Value("${gemini.model:gemini-2.0-flash}")
    private String model;

    @Override
    public AiItemResult infer(String keyword) {
        String apiKey = System.getenv("GEMINI_API_KEY");
        if (apiKey == null || apiKey.isBlank()) {
            throw new IllegalStateException("GEMINI_API_KEY is missing");
        }

        String prompt = """
            너는 식재료 표준화/분류기야.
            입력 키워드에서 식재료의 표준 이름, 카테고리ID, 유통기한(일 단위)을 추론해.
            반드시 아래 JSON만 출력해(다른 텍스트 금지).

            JSON 스키마(예시):
            {
              "normalizedName": "김치만두",
              "categoryId": 1,
              "expirationNum": 180
            }

            규칙:
            - expirationNum은 '일(day)' 단위 정수.
            - categoryId는 우리가 사용하는 food_categories의 ID라고 가정하고 가장 그럴듯한 값으로.
            - normalizedName은 브랜드/수식어 제거한 표준명(예: "비비고 김치만두" -> "김치만두").

            입력 키워드: %s
            """.formatted(keyword);

        Map<String, Object> body = Map.of(
            "contents", List.of(
                Map.of("parts", List.of(
                    Map.of("text", prompt)
                ))
            ),
            "generationConfig", Map.of(
                "temperature", 0.2,
                "maxOutputTokens", 512
            )
        );

        WebClient client = WebClient.builder()
            .baseUrl("https://generativelanguage.googleapis.com")
            .build();

        JsonNode root = client.post()
            .uri(uriBuilder -> uriBuilder
                .path("/v1beta/models/{model}:generateContent")
                .queryParam("key", apiKey)
                .build(model))
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(body)
            .retrieve()
            .bodyToMono(JsonNode.class)
            .timeout(Duration.ofSeconds(20))
            .block();

        if (root == null) {
            throw new IllegalStateException("Gemini response is null");
        }

        // 1) candidates[0].content.parts[*].text 모두 합치기
        String jsonText = extractTextFromParts(root);

        // 2) 비어있으면 fallback(parts[0].text)
        if (jsonText.isBlank()) {
            JsonNode textNode = root.at("/candidates/0/content/parts/0/text");
            if (!textNode.isMissingNode()) {
                jsonText = textNode.asText("");
            }
        }

        if (jsonText == null || jsonText.isBlank()) {
            throw new IllegalStateException("Gemini response text missing: " + root.toString());
        }

        // 3) code fence 제거 (fallback 포함해서 항상 한 번 더 정리)
        jsonText = stripMarkdownCodeFence(jsonText);

        // 4) JSON 파싱
        try {
            JsonNode j = objectMapper.readTree(jsonText);

            String normalizedName = requiredText(j, "normalizedName");
            long categoryId = requiredLong(j, "categoryId");
            long expirationNum = requiredLong(j, "expirationNum");

            return new AiItemResult(normalizedName, categoryId, expirationNum);
        } catch (Exception e) {
            throw new IllegalStateException("Failed to parse Gemini JSON: " + jsonText, e);
        }
    }

    private static String extractTextFromParts(JsonNode root) {
        JsonNode parts = root.at("/candidates/0/content/parts");
        if (!parts.isArray()) return "";

        StringBuilder sb = new StringBuilder();
        for (JsonNode p : parts) {
            JsonNode t = p.get("text");
            if (t != null && !t.asText().isBlank()) {
                sb.append(t.asText());
            }
        }
        return sb.toString().trim();
    }

    private static String stripMarkdownCodeFence(String s) {
        String t = (s == null) ? "" : s.trim();
        if (t.startsWith("```")) {
            t = t.replaceFirst("^```(json)?\\s*", "");
            t = t.replaceFirst("\\s*```\\s*$", "");
        }
        return t.trim();
    }

    private static String requiredText(JsonNode node, String field) {
        JsonNode v = node.get(field);
        if (v == null || v.asText().isBlank()) {
            throw new IllegalStateException("Missing field: " + field);
        }
        return v.asText().trim();
    }

    private static long requiredLong(JsonNode node, String field) {
        JsonNode v = node.get(field);
        if (v == null || !v.canConvertToLong()) {
            throw new IllegalStateException("Missing/invalid field: " + field);
        }
        return v.asLong();
    }
}
