package com.example.project.domain.recipe.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.project.domain.recipe.domain.RecipeIngredient;

public interface RecipeIngredientRepository extends JpaRepository<RecipeIngredient, Long> {
    // 특정 레시피에 속한 모든 재료 상세 정보 조회
    List<RecipeIngredient> findByRecipeRecipeId(Long recipeId);
}