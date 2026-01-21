package com.example.project.domain.fridge.ai;

import java.util.Map;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class RecommendAiClient {

    private final WebClient webClient;
    
    
    public RecommendAiClient(WebClient webClient) {
        this.webClient = webClient;
    }

    public String recommend(Map<String, Object> payload) {

        return webClient.post()
                .uri("/v1beta/models/gemini-2.0-flash:generateContent")
                .bodyValue(payload)
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }
}

