package com.example.project.domain.recipe.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.example.project.domain.fridge.domain.Items;
import com.example.project.domain.fridge.repository.FridgeItemRepository;
import com.example.project.domain.recipe.domain.Recipe;
import com.example.project.domain.recipe.domain.RecipeIngredient;
import com.example.project.domain.recipe.domain.UserDetail;
import com.example.project.domain.recipe.dto.IngredientDto;
import com.example.project.domain.recipe.dto.RecipeDetailResponse;
import com.example.project.domain.recipe.repository.RecipeRepository;
import com.example.project.domain.recipe.repository.UserDetailRepository;
import com.example.project.global.image.ImageUploadResponse;
import com.example.project.global.image.S3ImageService;
import com.example.project.member.domain.Users;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RecipeService {

    private final RecipeRepository recipeRepository;
    private final UserDetailRepository userDetailRepository;
    private final FridgeItemRepository fridgeItemRepository;
    private final RecipeAiClient recipeAiClient;
    
    private final S3ImageService s3ImageService;

    @Transactional(readOnly = true)
    public RecipeDetailResponse getRecipeDetailWithAiTip(Long recipeId, Users userDetails ) {
        Recipe recipe = recipeRepository.findById(recipeId)
                .orElseThrow(() -> new RuntimeException("레시피를 찾을 수 없습니다."));
        
        Long userId = userDetails.getUserId();
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
                .imageUrl(recipe.getRecipeImage().getImageUrl())
                .ingredients(ingredientDtos)
                .instructions(recipe.getInstructions())
                .personalTip(aiPersonalTip)
                .matchRate(calculateMatchRate(recipe, myIngredients))
                .missingIngredients(getMissingIngredients(recipe, myIngredients))
                .build();
    }
    
    
    
    
    @Transactional(readOnly = true)
    public RecipeDetailResponse getRecipeDetail(Long recipeId, Users userDetails) {
    	
    	
        Recipe recipe = recipeRepository.findById(recipeId)
                .orElseThrow(() -> new RuntimeException("레시피를 찾을 수 없습니다."));

        
        // FridgeItemRepository에 이 메서드가 정의되어 있어야 합니다. (아래 2번 참고)
        List<Items> myIngredients = fridgeItemRepository.findItemsByUserIdAndCategory(userDetails.getUserId(), 1L); // 예: 1L이 INGREDIENT 카테고리 ID일 경우

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


        return RecipeDetailResponse.builder()
                .recipeId(recipe.getRecipeId())
                .name(recipe.getName())
                .imageUrl(recipe.getRecipeImage().getImageUrl())
                .ingredients(ingredientDtos)
                .instructions(recipe.getInstructions())
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
    
    
 // [CREATE/UPDATE] 레시피 아이디만 받아서 이미지 저장
    @Transactional
    public void uploadRecipeImage(Long recipeId, MultipartFile file) {
        // 1. 레시피 존재 여부 확인 (없으면 에러)
        Recipe recipe = recipeRepository.findById(recipeId)
                .orElseThrow(() -> new RuntimeException("해당 레시피를 찾을 수 없습니다. ID: " + recipeId));

        // 2. 만약 기존에 이미지가 있었다면 S3에서 삭제 (덮어쓰기 방지 및 용량 관리)
        if (recipe.getRecipeImage() != null) {
            s3ImageService.delete(recipe.getRecipeImage().getS3Key());
        }

        // 3. S3 업로드 실행
        ImageUploadResponse res = s3ImageService.upload(file, "recipes");

        // 4. 레시피 엔티티에 이미지 정보 업데이트 (임베디드 필드 채우기)
        recipe.updateRecipeImage(res);

        // ※ @Transactional 덕분에 별도로 save를 안 해도 메서드 종료 시 DB에 반영됩니다.
    }

    // [DELETE] 레시피 전체 삭제 (이미지 포함)
    @Transactional
    public void deleteRecipe(Long recipeId) {
        Recipe recipe = recipeRepository.findById(recipeId)
                .orElseThrow(() -> new RuntimeException("레시피 없음"));

        // 1. S3에 저장된 실제 이미지 파일 삭제
        if (recipe.getRecipeImage() != null) {
            s3ImageService.delete(recipe.getRecipeImage().getS3Key());
        }

        // 2. DB에서 레시피 삭제
        recipeRepository.delete(recipe);
    }
    
}