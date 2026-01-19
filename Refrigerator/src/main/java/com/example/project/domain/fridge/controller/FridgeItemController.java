package com.example.project.domain.fridge.controller;

import org.springframework.web.bind.annotation.*;

import com.example.project.domain.fridge.dto.FridgeItemDto;
import com.example.project.domain.fridge.service.FridgeItemService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/fridge-items")
public class FridgeItemController {

    private final FridgeItemService fridgeItemService;

    @PostMapping
    public FridgeItemDto.Response create(
            @RequestParam Long userId,
            @RequestBody FridgeItemDto.Request req
    ) {
        return fridgeItemService.create(userId, req);
    }
}
