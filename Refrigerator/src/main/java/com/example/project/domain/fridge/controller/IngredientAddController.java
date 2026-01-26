package com.example.project.domain.fridge.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.project.domain.fridge.dto.IngredientCreateBulkRequest;
import com.example.project.domain.fridge.dto.IngredientCreateRequest;
import com.example.project.domain.fridge.dto.IngredientCreateResponse;
import com.example.project.domain.fridge.dto.IngredientResolveMultiRequest;
import com.example.project.domain.fridge.dto.IngredientResolveRequest;
import com.example.project.domain.fridge.dto.IngredientResolveResponse;
import com.example.project.domain.fridge.service.IngredientAddService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/fridge/ingredients")
@RequiredArgsConstructor
public class IngredientAddController {

    private final IngredientAddService ingredientAddService;

    // 사용자가 이름만 입력 → 매칭 결과 반환
    @PostMapping("/resolve")
    public ResponseEntity<IngredientResolveResponse> resolve(@RequestBody IngredientResolveRequest req) {
        return ResponseEntity.ok(ingredientAddService.resolve(req));
    }

    // 여러 개 한 번에 resolve 
    @PostMapping("/resolve/multi")
    public ResponseEntity<List<IngredientResolveResponse>> resolveMulti(@RequestBody IngredientResolveMultiRequest req) {
        return ResponseEntity.ok(ingredientAddService.resolveMulti(req));
    }

    // 사용자가 확정 + 상세 입력 → fridge_item 1개 생성
    @PostMapping
    public ResponseEntity<IngredientCreateResponse> create(@RequestBody IngredientCreateRequest req) {
        return ResponseEntity.ok(ingredientAddService.create(req));
    }

    // 사용자가 확정 + 상세 입력 → fridge_item 여러 개 생성 
    @PostMapping("/multi")
    public ResponseEntity<List<IngredientCreateResponse>> createMulti(@RequestBody IngredientCreateBulkRequest req) {
        return ResponseEntity.ok(ingredientAddService.createMulti(req));
    }
}
