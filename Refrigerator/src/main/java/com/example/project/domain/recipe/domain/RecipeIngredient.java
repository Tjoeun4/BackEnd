package com.example.project.domain.recipe.domain;

import java.math.BigDecimal;

import com.example.project.domain.fridge.domain.Items;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "recipe_ingredients")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RecipeIngredient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String ingredientName; // 재료명

    @Column(precision = 10, scale = 2)
    private BigDecimal count; // 개수 (예: 2, 0.5)

    @Column(precision = 10, scale = 2)
    private BigDecimal quantity; // 용량/수량 (예: 300, 500)

    @Column(length = 20)
    private String unit; // 단위 (예: 개, g, ml, 묶음)

    @Column(nullable = false)
    private boolean isMandatory; // 필수 여부

    private String substitutionName; // 대체 가능 재료

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recipe_id")
    private Recipe recipe;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Items item; // 냉장고 아이템 ID와 연결
}