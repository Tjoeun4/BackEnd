package com.example.project.domain.fridge.controller;

import java.util.List;
import java.util.Map;

import com.example.project.member.domain.Users;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import com.example.project.domain.fridge.dto.PantryItemDto;
import com.example.project.domain.fridge.service.PantryService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/fridge/pantry")
public class PantryController {

    private final PantryService pantryService;

    @GetMapping
    public ResponseEntity<List<PantryItemDto>> getPantry(
            @AuthenticationPrincipal Users userDetails
    ) {
        if (userDetails == null) {
            throw new IllegalStateException("인증이 필요합니다. Authorization: Bearer <accessToken> 헤더를 확인하세요.");
        }
        Long userId = userDetails.getUserId();
        pantryService.seedDefaultIfEmpty(userId);
        return ResponseEntity.ok(pantryService.getActivePantryItemDtos(userId));
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> addPantry(
        @RequestBody Map<String, String> body,
        @AuthenticationPrincipal Users userDetails
    ) {
        if (userDetails == null) {
            throw new IllegalStateException("인증이 필요합니다. Authorization: Bearer <accessToken> 헤더를 확인하세요.");
        }
        Long userId = userDetails.getUserId();
        String itemName = body != null ? body.get("itemName") : null;
        if (itemName == null || itemName.isBlank()) {
            return ResponseEntity.badRequest().body(Map.of("ok", false, "message", "itemName is required"));
        }
        
        String trimmedName = itemName.trim();
        
        // 추가 전에 중복 체크
        boolean isDuplicate = pantryService.isActiveItemExists(userId, trimmedName);
        if (isDuplicate) {
            return ResponseEntity.ok(Map.of("ok", false, "message", "이미 존재하는 항목입니다."));
        }
        
        // 중복이 아니면 추가
        pantryService.addPantryItem(userId, itemName);
        return ResponseEntity.ok(Map.of("ok", true, "message", "항목이 추가되었습니다."));
    }

    @DeleteMapping("/{pantryItemId}")
    public ResponseEntity<Map<String, Object>> removePantry(@PathVariable("pantryItemId") Long pantryItemId) {
        pantryService.removePantryItem(pantryItemId);
        return ResponseEntity.ok(Map.of("ok", true));
    }
}
