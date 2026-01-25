package com.example.project.domain.fridge.dto;

import java.util.List;

/**
 * pantry + FridgeItem 만으로 추천한 요리 3가지.
 * 각 Recipe: 사진(photoUrl), 레시피(steps), 재료(ingredients).
 */
public record RecommendResponse(Long userId, List<Recipe> recipes) {

    public record Recipe(
        String title,
        String summary,
        Integer timeMinutes,
        String difficulty,
        List<String> ingredients,
        List<String> steps,
        String photoUrl
    ) {}
}
