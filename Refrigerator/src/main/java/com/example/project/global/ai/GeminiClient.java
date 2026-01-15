package com.example.project.global.ai;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientResponseException;

import java.util.List;
import java.util.Map;

@Component
public class GeminiClient {

    private final RestClient restClient;
    private final String apiKey;
    private final String baseUrl;
    private final String model;

    public GeminiClient(
            RestClient restClient,
            @Value("${gemini.api-key}") String apiKey,
            @Value("${gemini.base-url}") String baseUrl,
            @Value("${gemini.model}") String model
    ) {
        this.restClient = restClient;
        this.apiKey = apiKey;
        this.baseUrl = baseUrl;
        this.model = model;
    }
    

    /**
     * 프롬프트를 Gemini에 전달하고
     * Gemini 원본 응답(JSON)을 Map으로 그대로 반환
     */
    public Map<String, Object> generate(String prompt) {
        String url = baseUrl + "/v1/models/" + model + ":generateContent?key=" + apiKey;

        Map<String, Object> body = Map.of(
                "contents", List.of(
                        Map.of(
                                "role", "user",
                                "parts", List.of(Map.of("text", prompt))
                        )
                )
        );

        try {
            return restClient.post()
                    .uri(url)
                    .header("Content-Type", "application/json")
                    .body(body)
                    .retrieve()
                    .body(Map.class);
        } catch (RestClientResponseException e) {
            // ✅ 여기서 Gemini가 준 에러 JSON이 그대로 찍힘
            System.out.println("❌ Gemini HTTP Status: " + e.getRawStatusCode());
            System.out.println("❌ Gemini Response Body: " + e.getResponseBodyAsString());
            throw e; // 던지는 건 그대로 (로직 변경 X)
        }
    }
}