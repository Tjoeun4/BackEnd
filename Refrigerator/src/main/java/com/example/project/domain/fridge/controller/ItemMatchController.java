package com.example.project.domain.fridge.controller;

import org.springframework.web.bind.annotation.*;

import com.example.project.domain.fridge.dto.ItemMatchDto;
import com.example.project.domain.fridge.service.ItemService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/items")
public class ItemMatchController {

    private final ItemService itemService;

    @PostMapping("/match")
    public ItemMatchDto.Response match(@RequestBody ItemMatchDto.Request req) {
        return itemService.match(req.getRawInputName());
    }
}
