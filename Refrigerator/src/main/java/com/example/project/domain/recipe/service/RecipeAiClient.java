package com.example.project.domain.recipe.service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.example.project.domain.fridge.domain.Items;
import com.example.project.domain.recipe.domain.Recipe;
import com.example.project.domain.recipe.domain.UserDetail;
import com.example.project.global.config.OcrClient;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class RecipeAiClient {
    
	@Value("${gemini.api-key}")
	private String apiKey;
    private final ObjectMapper objectMapper;
    private final RestTemplate restTemplate = new RestTemplate();


    private static final String GEMINI_API_URL = "https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-flash:generateContent";
    
    public String generatePersonalTip(Recipe recipe, UserDetail userDetail, List<Items> myIngredients) {
        try {
        	// Items 엔티티의 실제 필드명이 name, quantity, useByDate 인지 확인 필요
        	String fridgeContext = myIngredients.stream()
        		    .map(item -> String.format("- %s (카테고리 번호: %s)", 
        		        item.getName(), // getItemName() -> getName()
        		        item.getCategoryId())) // Items에 유통기한 필드가 없다면 FridgeItem에서 가져오도록 수정 필요
        		    .collect(Collectors.joining("\n"));

            // 레시피 전용 정밀 프롬프트
            String prompt = String.format("""
                당신은 사용자의 냉장고 상태와 입맛을 분석하여 조리 조언을 주는 요리 전문가입니다.
                다음 정보를 바탕으로 'personalTip'을 작성하여 JSON으로 응답하세요.

                [데이터]
                1. 레시피명: %s
                2. 기본 조리법: %s
                3. 사용자 취향: 숙련도(%s), 매운맛 선호(%d/5), 짠맛 선호(%d/5)
                4. 알러지: %s
                5. 현재 냉장고 재료 상태:
                %s

                [요구사항]
                - personalTip: 사용자의 입맛(맵기, 간)을 맞추는 법, 숙련도에 따른 주의점, 특히 '유통기한이 임박한 재료'를 우선 사용하라는 권장 사항을 포함하여 친절한 한 문장으로 작성하세요.
                - 알러지 재료가 있다면 주의 사항을 반드시 언급하세요.
                - 마크다운 없이 순수 JSON만 반환하세요. 형식: {"personalTip": "내용"}
                """, 
                recipe.getName(), recipe.getInstructions(), 
                userDetail.getCookingLevel(), userDetail.getSpicyLevel(), userDetail.getSaltyLevel(),
                String.join(", ", userDetail.getAllergies()),
                fridgeContext
            );

            Map<String, Object> requestBody = Map.of(
                "contents", List.of(
                    Map.of("parts", List.of(Map.of("text", prompt)))
                ),
                "generationConfig", Map.of("response_mime_type", "application/json") // JSON 응답 강제
            );

            String url = GEMINI_API_URL + "?key=" + apiKey;
            String responseStr = restTemplate.postForObject(url, requestBody, String.class);

            return parsePersonalTip(responseStr);

        } catch (Exception e) {
            return "평소 입맛에 맞춰 간을 조절하여 맛있게 요리해 보세요!"; // 예외 발생 시 기본 팁 반환
        }
    }

    private String parsePersonalTip(String responseStr) {
        try {
            JsonNode root = objectMapper.readTree(responseStr);
            String jsonText = root.path("candidates").get(0).path("content").path("parts").get(0).path("text").asText();
            JsonNode tipNode = objectMapper.readTree(jsonText);
            return tipNode.path("personalTip").asText();
        } catch (Exception e) {
            return "레시피 가이드에 따라 조리해 보세요.";
        }
    }
}