package com.example.project.domain.expense.service;

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
    
    @Value("${gemini.base-url}")
    private String baseUrl;

    @Value("${gemini.model}")
    private String model;

    private final OcrClient ocrClient;
    private final ObjectMapper objectMapper;
    private final RestTemplate restTemplate = new RestTemplate();

    public ReceiptAnalysisResponse analyzeReceiptImage(MultipartFile file) {
        try {
            System.out.println("\n[DEBUG] 1단계: OCR 텍스트 추출 시작...");
            String extractedText = ocrClient.getTextOnly(file);
            System.out.println("[DEBUG] OCR 결과 데이터: " + extractedText);

            if (extractedText == null || extractedText.trim().isEmpty() || extractedText.startsWith("Error:")) {
                System.out.println("[ERROR] OCR 추출 실패 혹은 에러 발생: " + extractedText);
                throw new RuntimeException("OCR 결과가 유효하지 않음");
            }

            System.out.println("[DEBUG] 2단계: Gemini 요청 바디 구성 중...");
            String url = String.format("%s/v1beta/models/%s:generateContent?key=%s", baseUrl, model, apiKey);
            
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
                  6. category: 다음 중 하나 (MEAL, INGREDIENT, READY_MEAL, DRINK, ETC, PANTRY)
                  7. isFridgeTarget: 식재료로서 냉장고 보관 대상이면 true, 아니면 false
            	  8. subCategory: 식재료의 상세 분류 (예: 육류, 야채, 수산물, 조미료, 가공식품 등 문맥에 따라 분류)
            	  9. sellByDate: (yyyy-MM-dd). 정보가 없으면 현재날짜 기준으로 찾아서 넣기.
                  10. useByDate: (yyyy-MM-dd). 정보가 ㅍ없으면 현재날짜 기준으로 찾아서 넣기.
                마크다운 없이 순수 JSON만 반환하세요.
                """;
            // 추출된 텍스트를 프롬프트에 합침
            String finalPrompt = prompt + "\n\n[분석할 영수증 데이터]:\n" + extractedText;

            // JSON 규격 Map 생성
            Map<String, Object> requestBody = Map.of(
                "contents", List.of(
                    Map.of("parts", List.of(
                        Map.of("text", finalPrompt)
                    ))
                )
            );

            System.out.println("[DEBUG] 3단계: Gemini API 호출 시도... (Model: " + model + ")");
            String responseStr = restTemplate.postForObject(url, requestBody, String.class);
            System.out.println("[DEBUG] Gemini API 호출 성공!");

            return parseGeminiResponse(responseStr);

        } catch (Exception e) {
            System.out.println("\n[!!! FATAL ERROR !!!] 처리 중 예외 발생:");
            e.printStackTrace(); // 콘솔에 상세 에러 스택을 빨간색으로 다 찍습니다.
            throw new RuntimeException("Gemini 분석 로직 실패", e);
        }
    }

    private ReceiptAnalysisResponse parseGeminiResponse(String responseStr) {
        try {
            System.out.println("[DEBUG] 4단계: 응답 JSON 파싱 시도...");
            JsonNode root = objectMapper.readTree(responseStr);
            String jsonText = root.path("candidates").get(0)
                                  .path("content")
                                  .path("parts").get(0)
                                  .path("text").asText().trim();
            
            System.out.println("[DEBUG] AI 응답 텍스트: " + jsonText);
            
            // 마크다운 제거 (```json 또는 ``` 제거)
            if (jsonText.startsWith("```")) {
                jsonText = jsonText.replaceAll("(?s)```(?:json)?\\n?(.*?)\\n?```", "$1");
                System.out.println("[DEBUG] 마크다운 제거 후 JSON: " + jsonText);
            }
            
            ReceiptAnalysisResponse result = objectMapper.readValue(jsonText, ReceiptAnalysisResponse.class);
            System.out.println("[DEBUG] DTO 매핑 완료! 상점명: " + result.getStoreName());
            
            return result;
        } catch (Exception e) {
            System.out.println("[ERROR] JSON 파싱 중 에러 발생! 원본 데이터 응답값 확인 필요.");
            System.out.println("[ERROR] 원본 응답: " + responseStr);
            e.printStackTrace();
            throw new RuntimeException("JSON 파싱 에러", e);
        }
    }
}