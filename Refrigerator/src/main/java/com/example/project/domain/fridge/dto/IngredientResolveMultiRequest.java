package com.example.project.domain.fridge.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class IngredientResolveMultiRequest {

    private Long userId;

    /** resolve할 이름 목록 (각각 단일 resolve와 동일 로직) */
    private List<String> inputNames;
}
