package com.example.project.domain.recipe.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.project.domain.recipe.dto.RecipeDetailResponse;
import com.example.project.domain.recipe.service.RecipeService;
import com.example.project.member.domain.Users;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/recipes")
@RequiredArgsConstructor
public class RecipeController {

    private final RecipeService recipeService; // RecipeService.java에서 정의된 빈 주입

    @GetMapping("/{recipeId}/details/{userId}")
    public ResponseEntity<RecipeDetailResponse> getRecipeDetails(
            @PathVariable Long recipeId, 
            @AuthenticationPrincipal Users userDetails // 1. 보안 객체로 변경
            
    		) {
        return ResponseEntity.ok(recipeService.getRecipeDetailWithAiTip(recipeId, userDetails));
    }
    
    
 // 레시피 이미지 업로드/수정
    @PostMapping("/{recipeId}/image")
    public ResponseEntity<String> updateImage(
            @PathVariable Long recipeId,
            @RequestPart("file") MultipartFile file) {
        
        recipeService.uploadRecipeImage(recipeId, file);
        return ResponseEntity.ok("레시피 사진 업데이트 성공");
    }

    // 레시피 상세 조회
    @GetMapping("/{recipeId}")
    public ResponseEntity<RecipeDetailResponse> getRecipe(
    		@PathVariable Long recipeId,
            @AuthenticationPrincipal Users userDetails // 1. 보안 객체로 변경

    		) {
        return ResponseEntity.ok(recipeService.getRecipeDetail(recipeId, userDetails));
    }

    // 레시피 삭제
    @DeleteMapping("/{recipeId}")
    public ResponseEntity<String> deleteRecipe(@PathVariable Long recipeId) {
        recipeService.deleteRecipe(recipeId);
        return ResponseEntity.ok("레시피 삭제 완료");
    }
    
}
