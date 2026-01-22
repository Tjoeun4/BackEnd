package com.example.project.domain.recipe.dto;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class IngredientDto {
    private Long itemId;         // 냉장고 아이템 ID와 매칭용
    private String name;         // 재료명 (예: 삼겹살)
    private BigDecimal count;    // 개수 (예: 2개)
    private BigDecimal quantity; // 용량/수량 (예: 300g)
    private String unit;         // 단위 (개, g, ml 등)
    private boolean isMandatory; // 필수 여부 (없으면 요리 불가 여부)
}