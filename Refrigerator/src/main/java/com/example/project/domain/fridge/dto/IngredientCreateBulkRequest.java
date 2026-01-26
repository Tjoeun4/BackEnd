package com.example.project.domain.fridge.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class IngredientCreateBulkRequest {

    /** 단일 추가와 동일: 사용자 ID */
    private Long userId;

    /** 추가할 식재료 목록 (각 항목 구조는 IngredientCreateRequest와 동일, userId 제외) */
    private List<IngredientCreateRequest> items;
}
