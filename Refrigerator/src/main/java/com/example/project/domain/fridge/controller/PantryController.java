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
        Long userId = userDetails.getUserId();
        pantryService.seedDefaultIfEmpty(userId);
        return ResponseEntity.ok(pantryService.getActivePantryItemDtos(userId));
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> addPantry(

        @RequestBody Map<String, String> body,
        @AuthenticationPrincipal Users userDetails
    ) {
        Long userId = userDetails.getUserId();
        String itemName = body != null ? body.get("itemName") : null;
        if (itemName == null || itemName.isBlank()) {
            return ResponseEntity.badRequest().body(Map.of("ok", false, "message", "itemName is required"));
        }
        pantryService.addPantryItem(userId, itemName);
        return ResponseEntity.ok(Map.of("ok", true));
    }

    @DeleteMapping("/{pantryItemId}")
    public ResponseEntity<Map<String, Object>> removePantry(@PathVariable("pantryItemId") Long pantryItemId) {
        pantryService.removePantryItem(pantryItemId);
        return ResponseEntity.ok(Map.of("ok", true));
    }
}
