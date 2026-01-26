package com.example.project.domain.fridge.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.project.domain.fridge.dto.FridgeItemDto;
import com.example.project.domain.fridge.service.FridgeService;
import com.example.project.member.domain.Users;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/fridge/items")
@RequiredArgsConstructor
public class FridgeController {

    private final FridgeService fridgeService;

    /**
     * 내 냉장고 재료 목록 조회 (유통기한 임박 순)
     */
    @GetMapping
    public ResponseEntity<List<FridgeItemDto>> getItems(
            @AuthenticationPrincipal Users user
    ) {
        Long userId = user.getUserId();
        return ResponseEntity.ok(fridgeService.getActiveItems(userId));
    }

    /**
     * 내 냉장고 재료 목록 조회 (정렬 없음)
     */
    @GetMapping("/plain")
    public ResponseEntity<List<FridgeItemDto>> getItemsPlain(
            @AuthenticationPrincipal Users user
    ) {
        Long userId = user.getUserId();
        return ResponseEntity.ok(fridgeService.getActiveItemsOnly(userId));
    }

    /**
     * 냉장고 재료 삭제 (소프트 삭제). 본인 소유만 가능.
     */
    @DeleteMapping("/{fridgeItemId}")
    public ResponseEntity<Void> removeItem(
            @PathVariable Long fridgeItemId,
            @AuthenticationPrincipal Users user
    ) {
        Long userId = user.getUserId();
        fridgeService.remove(userId, fridgeItemId);
        return ResponseEntity.noContent().build();
    }
}
