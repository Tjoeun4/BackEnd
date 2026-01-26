package com.example.project.domain.fridge.controller;

import com.example.project.domain.fridge.dto.*;
import com.example.project.domain.fridge.service.IngredientAddService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import com.example.project.member.domain.Users;


@RestController
@RequestMapping("/api/fridge/ingredients")
@RequiredArgsConstructor
public class IngredientAddController {

    private final IngredientAddService ingredientAddService;

    // 사용자가 이름만 입력 → 매칭 결과 반환
     
    @PostMapping("/resolve")
    public ResponseEntity<IngredientResolveResponse> resolve(
            @RequestBody IngredientResolveRequest req,
           @AuthenticationPrincipal Users userDetails
    ) {
        Long userId = userDetails.getUserId();
        req.setUserId(userId);
        return ResponseEntity.ok(ingredientAddService.resolve(req));
    }

    // 사용자가 확정 + 상세 입력 → fridge_item 생성
     
    @PostMapping
    public ResponseEntity<IngredientCreateResponse> create(
            @RequestBody IngredientCreateRequest req,
            @AuthenticationPrincipal Users userDetails
    ) {
        Long userId = userDetails.getUserId();
        req.setUserId(userId);
        return ResponseEntity.ok(ingredientAddService.create(req));
    }
}
