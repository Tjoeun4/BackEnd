package com.example.project.global.ai;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

@Component
@RequiredArgsConstructor
public class GeminiClient {

    private final WebClient geminiWebClient;
    private final GeminiProperties props;

    private final ObjectMapper objectMapper = new ObjectMapper();

    public AiItemInfo inferItemInfo(String userInputName) {
        String model = props.model();
        String path = "/v1beta/models/" + model + ":generateContent";

        // JSON만 출력 강제
        String prompt = """
            너는 식재료 DB 구축 도우미야.
            사용자가 입력한 식재료 이름을 표준화해서 items 테이블에 넣을 정보를 만든다.

            아래 JSON만 출력해. 다른 텍스트/설명/코드블록/마크다운 금지.
            JSON 스키마:
            {
              "itemName": "표준식재료명(한국어, 2~20자)",
              "expirationDays": 1~365 정수,
              "categoryName": "육류|양념|채소|유제품|해산물|과일 중 하나",
              "confidence": 0.0~1.0 숫자,
              "notes": "가정/근거 1줄"
            }

            categoryName은 반드시 다음 6개 중 정확히 하나만 사용:
            [육류, 양념, 채소, 유제품, 해산물, 과일]

            사용자 입력: "%s"
            """.formatted(userInputName);

        try {
            // 요청 body 구성
            var body = objectMapper.createObjectNode();
            var contents = body.putArray("contents");
            var c0 = contents.addObject();
            var parts = c0.putArray("parts");
            parts.addObject().put("text", prompt);

            String raw = geminiWebClient.post()
                .uri(uriBuilder -> uriBuilder
                    .path(path)
                    .queryParam("key", props.apiKey())
                    .build()
                )
                .bodyValue(body)
                .retrieve()
                // ✅ HTTP 에러면 바디까지 포함해서 예외로 던지기
                .onStatus(HttpStatusCode::isError, resp ->
                    resp.bodyToMono(String.class).map(errBody ->
                        new RuntimeException("Gemini HTTP error: " + resp.statusCode() + " body=" + errBody)
                    )
                )
                .bodyToMono(String.class)
                .block();

            if (raw == null || raw.isBlank()) {
                throw new IllegalStateException("Gemini response empty");
            }

            // 1) candidates 텍스트 추출
            JsonNode root = objectMapper.readTree(raw);
            JsonNode textNode = root.at("/candidates/0/content/parts/0/text");
            if (textNode.isMissingNode()) {
                // Gemini가 blocked/candidate 없을 때도 있으니 원문 같이 던짐
                throw new IllegalStateException("Gemini response missing candidates text. raw=" + raw);
            }

            String text = textNode.asText().trim();

            // 2) 안전하게 JSON만 뽑기 (앞뒤 잡텍스트/코드블록 제거)
            String jsonOnly = extractJsonObject(text);

            // 3) JSON 파싱
            JsonNode info = objectMapper.readTree(jsonOnly);

            String itemName = info.path("itemName").asText(null);
            Integer expirationDays = info.path("expirationDays").isNumber() ? info.path("expirationDays").asInt() : null;
            String categoryName = info.path("categoryName").asText(null);
            Double confidence = info.path("confidence").isNumber() ? info.path("confidence").asDouble() : null;
            String notes = info.path("notes").asText(null);

            return new AiItemInfo(itemName, expirationDays, categoryName, confidence, notes);

        } catch (WebClientResponseException e) {
            // ✅ 여기가 찍히면 상태코드/바디가 다 나옴
            throw new RuntimeException("Gemini HTTP exception: status=" + e.getStatusCode()
                + " body=" + e.getResponseBodyAsString(), e);

        } catch (Exception e) {
            // ✅ 원인 파악용: 메시지를 더 자세히
            throw new RuntimeException("Gemini infer failed: " + e.getMessage(), e);
        }
    }

    /**
     * Gemini가 JSON 외 텍스트를 섞어 보내도,
     * 첫 번째 {...} JSON 오브젝트만 뽑아내는 유틸.
     */
    private String extractJsonObject(String s) {
        if (s == null) return null;

        // 코드블록 ``` 제거(있으면)
        String t = s.replace("```json", "").replace("```", "").trim();

        int start = t.indexOf('{');
        if (start < 0) throw new IllegalStateException("No JSON object start found. text=" + t);

        int depth = 0;
        for (int i = start; i < t.length(); i++) {
            char c = t.charAt(i);
            if (c == '{') depth++;
            if (c == '}') depth--;
            if (depth == 0) {
                return t.substring(start, i + 1).trim();
            }
        }
        throw new IllegalStateException("No JSON object end found. text=" + t);
    }

    public record AiItemInfo(
        String itemName,
        Integer expirationDays,
        String categoryName,
        Double confidence,
        String notes
    ) {}
}
