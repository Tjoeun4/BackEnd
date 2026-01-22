package com.example.project.domain.fridge.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class IngredientResolveRequest {
    private Long userId;
    private String inputName;
}
