package com.example.project.domain.recipe.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.project.domain.recipe.dto.RecipeDetailResponse;
import com.example.project.domain.recipe.service.RecipeService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/recipes")
@RequiredArgsConstructor
public class RecipeController {

    private final RecipeService recipeService; // RecipeService.java에서 정의된 빈 주입

    @GetMapping("/{recipeId}/details/{userId}")
    public ResponseEntity<RecipeDetailResponse> getRecipeDetails(
            @PathVariable Long recipeId, 
            @PathVariable Long userId) {
        return ResponseEntity.ok(recipeService.getRecipeDetailWithAiTip(recipeId, userId));
    }
}
