package com.example.project.domain.fridge.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.Setter;


@Setter
@Getter
@NoArgsConstructor
public class IngredientResolveRequest {
    private Long userId;
    private String inputName;
}
