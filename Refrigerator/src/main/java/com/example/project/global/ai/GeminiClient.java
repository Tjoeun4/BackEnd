package com.example.project.global.ai;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
@RequiredArgsConstructor
public class GeminiClient {

    private final WebClient geminiWebClient;
    private final GeminiProperties props;

    private final ObjectMapper objectMapper = new ObjectMapper();

    public AiItemInfo inferItemInfo(String userInputName) {
        try {
            String model = props.model();
            String path = "/v1beta/models/" + model + ":generateContent";

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

            // 요청 바디 구성
            var body = objectMapper.createObjectNode();
            var contents = body.putArray("contents");
            var c0 = contents.addObject();
            var parts = c0.putArray("parts");
            parts.addObject().put("text", prompt);

            // ✅ 키를 query param으로 붙여서 호출
            String raw = geminiWebClient.post()
                .uri(uriBuilder -> uriBuilder
                    .path(path)
                    .queryParam("key", props.apiKey())
                    .build()
                )
                .bodyValue(body)
                .retrieve()
                .bodyToMono(String.class)
                .block();

            if (raw == null || raw.isBlank()) {
                throw new IllegalStateException("Gemini response empty");
            }

            // 응답에서 candidates[0].content.parts[0].text 추출
            JsonNode root = objectMapper.readTree(raw);
            JsonNode textNode = root.at("/candidates/0/content/parts/0/text");
            if (textNode.isMissingNode()) {
                throw new IllegalStateException("Gemini response missing text: " + raw);
            }

            String jsonText = textNode.asText().trim();

            // 혹시 앞뒤에 잡텍스트가 붙는 케이스 대비(테스트 단계용)
            JsonNode info = objectMapper.readTree(jsonText);

            return new AiItemInfo(
                info.path("itemName").asText(null),
                info.path("expirationDays").isNumber() ? info.path("expirationDays").asInt() : null,
                info.path("categoryName").asText(null),
                info.path("confidence").isNumber() ? info.path("confidence").asDouble() : null,
                info.path("notes").asText(null)
            );

        } catch (Exception e) {
            throw new RuntimeException("Gemini infer failed", e);
        }
    }

    public record AiItemInfo(
        String itemName,
        Integer expirationDays,
        String categoryName,
        Double confidence,
        String notes
    ) {}
}
