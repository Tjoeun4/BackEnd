package com.example.project.domain.expense.service;

import java.io.IOException;
import java.util.Base64;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import com.example.project.domain.expense.dto.ReceiptAnalysisResponse;
import com.example.project.global.config.OcrClient;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ReceiptGeminiClient {

    @Value("${google.gemini.api.key}")
    private String apiKey;

    private final OcrClient ocrClient;
    private final ObjectMapper objectMapper;
    private final RestTemplate restTemplate = new RestTemplate();

    private static final String GEMINI_API_URL = "https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-flash:generateContent";

    public ReceiptAnalysisResponse analyzeReceiptImage(MultipartFile file) {
        try {
        	String text1 = ocrClient.getTextOnly(file);
        	
        	
//            String base64Image = Base64.getEncoder().encodeToString(file.getBytes());

         // ReceiptGeminiClient.java 내부 프롬프트 부분 수정
            String prompt = """
                영수증 이미지를 분석하여 JSON으로 응답하세요. 
                - storeName, totalAmount, purchasedAt(yyyy-MM-ddTHH:mm:ss), summaryMemo 포함
                - items 리스트 내부 필수 필드:
                  1. itemName: 상품명
                  2. unitAmount: 단가 (숫자)
                  3. quantity: 수량 (숫자)
                  4. amount: 용량 또는 단위 (예: "500g", "360ml", "1묶음"). 정보가 없으면 null.
                  5. price: 해당 항목 총액 (숫자)
                  6. category: 다음 중 하나 (MEAL, INGREDIENT, READY_MEAL, DRINK, ETC)
                  7. isFridgeTarget: 식재료로서 냉장고 보관 대상이면 true, 아니면 false
            	  8. subCategory: 식재료의 상세 분류 (예: 육류, 야채, 수산물, 조미료, 가공식품 등 문맥에 따라 분류)
            	  9. sellByDate: 영수증에 표시된 유통기한 (yyyy-MM-dd). 정보가 없으면 null.
                  10. useByDate: 영수증에 표시된 소비기한 (yyyy-MM-dd). 정보가 없으면 null.
                마크다운 없이 순수 JSON만 반환하세요.
                """;

//            Map<String, Object> requestBody = Map.of(
//                "contents", List.of(
//                    Map.of("parts", List.of(
//                        Map.of("text", prompt),
//                        Map.of("inline_data", Map.of("mime_type", "image/jpeg", "data", base64Image))
//                    ))
//                )
//            );

            String url = GEMINI_API_URL + "?key=" + apiKey;
            String responseStr = restTemplate.postForObject(url, text1, String.class);

            return parseGeminiResponse(responseStr);
        } catch (Exception e) {
            throw new RuntimeException("파일 처리 실패", e);
        }
    }

    private ReceiptAnalysisResponse parseGeminiResponse(String responseStr) {
        try {
            JsonNode root = objectMapper.readTree(responseStr);
            String jsonText = root.path("candidates").get(0).path("content").path("parts").get(0).path("text").asText();
            
            // 일반 클래스 DTO로 매핑
            return objectMapper.readValue(jsonText, ReceiptAnalysisResponse.class);
        } catch (Exception e) {
            throw new RuntimeException("JSON 파싱 실패", e);
        }
    }
}