package com.example.project.domain.fridge.controller;

import com.example.project.domain.fridge.dto.RecommendResponse;
import com.example.project.domain.fridge.service.RecommendService;
import com.example.project.member.domain.Users;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/fridge")
public class RecommendController {

    private final RecommendService recommendService;

    @GetMapping("/recommend")
    public RecommendResponse recommend(
            @AuthenticationPrincipal Users userDetails
    ) {
        Long userId = userDetails.getUserId();
        return recommendService.recommend3OnlyAllowed(userId);
    }
}
