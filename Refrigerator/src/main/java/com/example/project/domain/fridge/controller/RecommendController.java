package com.example.project.domain.fridge.controller;

import com.example.project.domain.fridge.dto.RecommendResponse;
import com.example.project.domain.fridge.service.RecommendService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/fridge")
public class RecommendController {

    private final RecommendService recommendService;

    @GetMapping("/recommend")
    public RecommendResponse recommend(@RequestParam("userId") Long userId) {
        return recommendService.recommend3OnlyAllowed(userId);
    }
}
