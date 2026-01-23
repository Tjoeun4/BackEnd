package com.example.project.domain.recipe.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RecipeDetailResponse {
    private Long recipeId;
    private String name;
    private String imageUrl;
    
    // DB에서 가져온 정석 데이터 리스트
    private List<IngredientDto> ingredients;
    private String instructions; 
    
    // AI가 생성한 개인 맞춤형 비고
    private String personalTip; 
    
    // 시스템 계산 데이터
    private Double matchRate;               // 재료 충족률 (%)
    private List<String> missingIngredients; // 부족해서 따로 사야 할 재료 목록
}
