package com.example.project.domain.fridge.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
public class IngredientCreateRequest {
    private Long userId;

    // 사용자가 입력한 원문
    private String inputName;

    // alias 확정이면 itemAliasId로 처리
    private Long itemAliasId;

    // items 후보에서 선택이면 itemId로 처리
    private Long itemId;

    private BigDecimal quantity;
    private String unit;
    private LocalDate purchaseDate;
}
