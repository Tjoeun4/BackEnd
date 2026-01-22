package com.example.project.domain.recipe.dto;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserPreferenceRequest {
    private String cookingLevel; // 초보, 중수, 고수
    private Integer spicyLevel;  // 1~5
    private Integer saltyLevel;  // 1~5
    private List<String> allergies; // 알러지 재료 리스트
}
