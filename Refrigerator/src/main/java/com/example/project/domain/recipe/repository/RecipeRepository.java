package com.example.project.domain.recipe.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.project.domain.recipe.domain.Recipe;

public interface RecipeRepository extends JpaRepository<Recipe, Long> {
    
    // 알러지 재료가 포함된 레시피를 제외하고 조회하는 예시 (JPQL)
    @Query("SELECT r FROM Recipe r WHERE r.recipeId NOT IN " +
           "(SELECT ri.recipe.recipeId FROM RecipeIngredient ri WHERE ri.ingredientName IN :allergies)")
    List<Recipe> findAllExcludingAllergies(@Param("allergies") List<String> allergies);
}
