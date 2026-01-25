package com.example.project.domain.fridge.service;

import com.example.project.domain.fridge.dto.AiItemInfo;
import com.example.project.domain.fridge.prompt.InferItemPrompt;
import com.example.project.global.ai.GeminiClient;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class IngredientInferClient {

    private final GeminiClient geminiClient;
    private final ObjectMapper objectMapper;

    public AiItemInfo inferItemInfo(String userInputName) {
        try {
            String prompt = InferItemPrompt.build(userInputName);
            String text = geminiClient.generateText(prompt);
            String jsonOnly = geminiClient.extractJsonObject(text);

            JsonNode info = objectMapper.readTree(jsonOnly);

            String itemName = info.path("itemName").asText(null);
            Integer expirationDays = info.path("expirationDays").isNumber() ? info.path("expirationDays").asInt() : null;
            String categoryName = info.path("categoryName").asText(null);
            Double confidence = info.path("confidence").isNumber() ? info.path("confidence").asDouble() : null;
            String notes = info.path("notes").asText(null);

            return new AiItemInfo(itemName, expirationDays, categoryName, confidence, notes);

        } catch (Exception e) {
            throw new RuntimeException("Gemini infer failed: " + e.getMessage(), e);
        }
    }
}
