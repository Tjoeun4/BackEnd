package com.example.project.domain.fridge.service;

import com.example.project.domain.fridge.domain.FridgeItem;
import com.example.project.domain.fridge.dto.RecommendResponse;
import com.example.project.domain.fridge.repository.FridgeItemRepository;
import com.example.project.global.ai.GeminiClient;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Service
@RequiredArgsConstructor
public class RecommendService {

    private final FridgeItemRepository fridgeItemRepository;
    private final PantryService pantryService;
    private final GeminiClient geminiClient;

    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * ✅ 냉장고 + 팬트리만으로 요리 3개 추천
     * ❌ 추가 재료 절대 불가
     */
    @Transactional(readOnly = true)
    public RecommendResponse recommend3OnlyAllowed(Long userId) {
        if (userId == null) throw new IllegalArgumentException("userId is required");

        pantryService.seedDefaultIfEmpty(userId);
        Set<String> pantryNames = pantryService.getActivePantryNames(userId);

        List<FridgeItem> fridgeItems = fridgeItemRepository.findActiveItems(userId);
        if (fridgeItems.isEmpty()) {
            throw new IllegalArgumentException("냉장고에 재료가 없으면 추천할 수 없습니다.");
        }
        List<Map<String, Object>> fridgePayload = buildFridgePayload(fridgeItems);

        Set<String> allowed = buildAllowedSet(fridgeItems, pantryNames);

        String prompt = buildPrompt(fridgePayload, pantryNames);
        String aiText = geminiClient.generateText(prompt);

        RecommendResponse parsed = parseToResponse(userId, aiText);

        if (!isValid(parsed, fridgeItems, allowed)) {
            String retryPrompt = buildRetryPrompt(fridgePayload, pantryNames, parsed);
            String retryText = geminiClient.generateText(retryPrompt);
            parsed = parseToResponse(userId, retryText);

            if (!isValid(parsed, fridgeItems, allowed)) {
                throw new RuntimeException("AI 추천이 규칙을 위반했습니다. (허용 재료 외 사용/형식 오류)");
            }
        }

        return parsed;
    }

    /* ---------------- Payload ---------------- */

    private List<Map<String, Object>> buildFridgePayload(List<FridgeItem> items) {
        LocalDate today = LocalDate.now();
        List<Map<String, Object>> out = new ArrayList<>();

        for (FridgeItem f : items) {
            Integer daysLeft = null;
            if (f.getExpiryDate() != null) {
                daysLeft = (int) ChronoUnit.DAYS.between(today, f.getExpiryDate());
            }

            String itemName = (f.getItem() != null) ? f.getItem().getName() : null;

            Map<String, Object> one = new LinkedHashMap<>();
            one.put("itemName", itemName);
            one.put("rawName", f.getRawName());
            one.put("quantity", f.getQuantity());
            one.put("unit", f.getUnit());
            one.put("expiryDate", f.getExpiryDate() == null ? null : f.getExpiryDate().toString());
            one.put("daysLeft", daysLeft);

            out.add(one);
        }

        
        out.sort(Comparator.comparing(m -> (Integer) m.get("daysLeft"), Comparator.nullsLast(Integer::compareTo)));
        return out;
    }

    private Set<String> buildAllowedSet(List<FridgeItem> fridgeItems, Set<String> pantryNames) {
        Set<String> allowed = new HashSet<>(pantryNames);
        for (FridgeItem f : fridgeItems) {
            if (f.getRawName() != null && !f.getRawName().isBlank()) {
                allowed.add(f.getRawName().trim());
            }
            if (f.getItem() != null && f.getItem().getName() != null && !f.getItem().getName().isBlank()) {
                allowed.add(f.getItem().getName().trim());
            }
        }
        return allowed;
    }

    private String toJson(Object o) {
        try { return objectMapper.writeValueAsString(o); }
        catch (Exception e) { return "[]"; }
    }

    /* ---------------- Prompt ---------------- */

    private String buildPrompt(List<Map<String, Object>> fridge, Set<String> pantryNames) {
        return """
        너는 냉장고+팬트리 재료만으로 요리 추천하는 AI다.

        🚨 절대 규칙:
        1) "팬트리" + "냉장고 재료" 외 재료는 절대 사용 금지.
        2) 추가 재료 구매 불가. ingredients·steps 어디에도 목록 밖 재료 금지.
        3) 각 요리는 냉장고 재료를 1개 이상 사용 (팬트리만으로는 불가).
        4) 요리 정확히 3개. 
        5) 아래 JSON만 출력. 다른 텍스트/코드블록/마크다운 금지.

        팬트리: %s

        냉장고 재료(JSON): %s

        출력 JSON 스키마:
        {
          "recipes": [
            {
              "title": "요리명",
              "summary": "한줄 설명",
              "estimatedMinutes": 5~90 정수,
              "difficulty": "EASY|MEDIUM|HARD",
              "ingredients": ["재료명 수량단위(예: 당근 2개, 양파 1개, 마늘 3쪽, 식용유 1큰술)"],
              "steps": ["조리 단계 1", "조리 단계 2", "..."]
            }
          ]
        }
        
        중요: ingredients 배열의 각 문자열은 반드시 "재료명 수량단위" 형식이어야 합니다.
        예시: "당근 2개", "양파 1개", "마늘 3쪽", "식용유 1큰술", "소금 적당량"
        """.formatted(String.join(", ", pantryNames), toJson(fridge));
    }

    private String buildRetryPrompt(List<Map<String, Object>> fridge, Set<String> pantryNames, RecommendResponse bad) {
        return """
        이전 응답이 규칙을 위반했다. 다시 생성해.

        🚨 절대 규칙: 팬트리+냉장고 재료 외 재료 금지. 각 요리 냉장고 재료 최소 1개. 요리 3개. JSON만 출력.

        팬트리: %s
        냉장고 재료(JSON): %s
        (참고) 이전 잘못된 응답: %s

        출력 스키마: { "recipes": [ { "title", "summary", "estimatedMinutes", "difficulty", "ingredients": ["재료명 수량단위"], "steps": ["..."] } ] }
        """.formatted(String.join(", ", pantryNames), toJson(fridge), safeBadSummary(bad));
    }

    private String safeBadSummary(RecommendResponse bad) {
        try { return objectMapper.writeValueAsString(bad); }
        catch (Exception e) { return "(unavailable)"; }
    }

    /* ---------------- Parse ---------------- */

    private RecommendResponse parseToResponse(Long userId, String aiText) {
        try {
            String jsonOnly = geminiClient.extractJsonObject(aiText);
            JsonNode root = objectMapper.readTree(jsonOnly);

            List<RecommendResponse.Recipe> recipes = new ArrayList<>();
            for (JsonNode r : root.path("recipes")) {
                String title = r.path("title").asText(null);
                List<String> ingredients = toStringList(r.path("ingredients"));
                List<String> steps = toStringList(r.path("steps"));
                String photoUrl = buildPhotoUrl(title);

                recipes.add(new RecommendResponse.Recipe(
                    title,
                    r.path("summary").asText(null),
                    r.path("estimatedMinutes").isNumber() ? r.path("estimatedMinutes").asInt() : null,
                    r.path("difficulty").asText(null),
                    ingredients,
                    steps,
                    photoUrl
                ));
            }

            return new RecommendResponse(userId, recipes);
        } catch (Exception e) {
            throw new RuntimeException("Recommend parse failed: " + e.getMessage(), e);
        }
    }

    private String buildPhotoUrl(String title) {
        int seed = (title != null) ? Math.abs(title.hashCode()) : 0;
        return "https://picsum.photos/seed/" + seed + "/400/300";
    }

    private List<String> toStringList(JsonNode n) {
        if (n == null || !n.isArray()) return List.of();
        List<String> out = new ArrayList<>();
        for (JsonNode x : n) out.add(x.asText());
        return out;
    }

    /* ---------------- Validation ---------------- */

    private boolean isValid(RecommendResponse res, List<FridgeItem> fridgeItems, Set<String> allowed) {
        if (res == null || res.recipes() == null) return false;
        if (res.recipes().size() != 3) return false;

        // 냉장고 재료명(검증: 각 레시피가 fridge 재료를 최소 1개 써야 함)
        Set<String> fridgeNames = new HashSet<>();
        for (FridgeItem f : fridgeItems) {
            if (f.getRawName() != null && !f.getRawName().isBlank()) fridgeNames.add(f.getRawName().trim());
            if (f.getItem() != null && f.getItem().getName() != null && !f.getItem().getName().isBlank()) {
                fridgeNames.add(f.getItem().getName().trim());
            }
        }

        for (RecommendResponse.Recipe r : res.recipes()) {
            if (r.title() == null || r.title().isBlank()) return false;
            if (r.ingredients() == null || r.ingredients().isEmpty()) return false;
            if (r.steps() == null || r.steps().isEmpty()) return false;

            // 재료 문자열에서 재료명만 추출하여 검증 (예: "당근 2개" -> "당근")
            boolean usesAtLeastOneFridgeItem = r.ingredients().stream()
                .filter(Objects::nonNull)
                .map(ing -> extractIngredientName(ing))
                .filter(Objects::nonNull)
                .map(String::trim)
                .anyMatch(fridgeNames::contains);
            if (!usesAtLeastOneFridgeItem) return false;

            for (String ing : r.ingredients()) {
                if (ing == null) continue;
                String name = extractIngredientName(ing);
                if (name != null && !allowed.contains(name.trim())) return false;
            }
        }

        return true;
    }

    /**
     * 재료 문자열에서 재료명만 추출 (예: "당근 2개" -> "당근")
     */
    private String extractIngredientName(String ingredient) {
        if (ingredient == null || ingredient.isBlank()) return null;
        
        // 숫자, 공백, 단위 등을 제거하고 재료명만 추출
        // 예: "당근 2개" -> "당근", "양파 1개" -> "양파"
        String trimmed = ingredient.trim();
        
        // 공백으로 분리하여 첫 번째 단어가 재료명일 가능성이 높음
        String[] parts = trimmed.split("\\s+");
        if (parts.length > 0) {
            return parts[0].trim();
        }
        
        return trimmed;
    }
}
