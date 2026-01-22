package com.example.project.domain.recipe.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.project.domain.fridge.domain.Items;
import com.example.project.domain.fridge.repository.FridgeItemRepository;
import com.example.project.domain.recipe.domain.Recipe;
import com.example.project.domain.recipe.domain.RecipeIngredient;
import com.example.project.domain.recipe.domain.UserDetail;
import com.example.project.domain.recipe.dto.IngredientDto;
import com.example.project.domain.recipe.dto.RecipeDetailResponse;
import com.example.project.domain.recipe.repository.RecipeRepository;
import com.example.project.domain.recipe.repository.UserDetailRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RecipeService {

    private final RecipeRepository recipeRepository;
    private final UserDetailRepository userDetailRepository;
    private final FridgeItemRepository fridgeItemRepository;
    private final RecipeAiClient recipeAiClient;

    @Transactional(readOnly = true)
    public RecipeDetailResponse getRecipeDetailWithAiTip(Long recipeId, Long userId) {
        Recipe recipe = recipeRepository.findById(recipeId)
                .orElseThrow(() -> new RuntimeException("레시피를 찾을 수 없습니다."));
        UserDetail userDetail = userDetailRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("유저 정보를 찾을 수 없습니다."));

        validateAllergy(recipe, userDetail);

        // FridgeItemRepository에 이 메서드가 정의되어 있어야 합니다. (아래 2번 참고)
        List<Items> myIngredients = fridgeItemRepository.findItemsByUserIdAndCategory(userId, 1L); // 예: 1L이 INGREDIENT 카테고리 ID일 경우

        List<IngredientDto> ingredientDtos = recipe.getRecipeIngredients().stream()
                .map(ri -> IngredientDto.builder()
                        .itemId(ri.getItem() != null ? ri.getItem().getId() : null) // .getId()로 수정
                        .name(ri.getIngredientName())
                        .count(ri.getCount())
                        .quantity(ri.getQuantity())
                        .unit(ri.getUnit())
                        .isMandatory(ri.isMandatory())
                        .build())
                .collect(Collectors.toList());

        String aiPersonalTip = recipeAiClient.generatePersonalTip(recipe, userDetail, myIngredients);

        return RecipeDetailResponse.builder()
                .recipeId(recipe.getRecipeId())
                .name(recipe.getName())
                .imageUrl(recipe.getImageUrl())
                .ingredients(ingredientDtos)
                .instructions(recipe.getInstructions())
                .personalTip(aiPersonalTip)
                .matchRate(calculateMatchRate(recipe, myIngredients))
                .missingIngredients(getMissingIngredients(recipe, myIngredients))
                .build();
    }

    private void validateAllergy(Recipe recipe, UserDetail userDetail) {
        List<String> userAllergies = userDetail.getAllergies();
        boolean hasAllergy = recipe.getRecipeIngredients().stream()
                .filter(RecipeIngredient::isMandatory)
                .anyMatch(ri -> userAllergies.contains(ri.getIngredientName()));
        if (hasAllergy) throw new RuntimeException("알러지 유발 재료가 포함된 레시피입니다.");
    }

    private List<String> getMissingIngredients(Recipe recipe, List<Items> myItems) {
        List<Long> myItemIds = myItems.stream()
                .map(Items::getId) // .getId()로 수정
                .collect(Collectors.toList());
        
        return recipe.getRecipeIngredients().stream()
                .filter(RecipeIngredient::isMandatory)
                .filter(ri -> ri.getItem() == null || !myItemIds.contains(ri.getItem().getId()))
                .map(RecipeIngredient::getIngredientName)
                .collect(Collectors.toList());
    }

    private Double calculateMatchRate(Recipe recipe, List<Items> myItems) {
        List<Long> requiredIds = recipe.getRecipeIngredients().stream()
                .filter(RecipeIngredient::isMandatory)
                .map(ri -> ri.getItem() != null ? ri.getItem().getId() : -1L)
                .collect(Collectors.toList());

        if (requiredIds.isEmpty()) return 100.0;

        List<Long> myItemIds = myItems.stream()
                .map(Items::getId) // .getId()로 수정
                .collect(Collectors.toList());
                
        long matchCount = requiredIds.stream()
                .filter(myItemIds::contains)
                .count();

        return (double) matchCount / requiredIds.size() * 100;
    }
}