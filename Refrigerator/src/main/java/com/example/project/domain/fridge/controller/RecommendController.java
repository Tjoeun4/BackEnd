//package com.example.project.domain.fridge.controller;
//
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import com.example.project.domain.fridge.dto.RecommendRequestDto;
//import com.example.project.domain.fridge.dto.RecommendResponseDto;
//import com.example.project.domain.fridge.service.RecommendService;
//
//@RestController
//@RequestMapping("/api/fridge/recommend")
//public class RecommendController {
//
//    private final RecommendService recommendService;
//
//    public RecommendController(RecommendService recommendService) {
//        this.recommendService = recommendService;
//    }
//
//    @PostMapping
//    public ResponseEntity<RecommendResponseDto> recommend(
//            @RequestParam Long userId,
//            @RequestBody(required = false) RecommendRequestDto request
//    ) {
//        // request를 안 보내면(null) 기본값으로 추천 돌리기
//        RecommendResponseDto response = recommendService.recommend(userId, request);
//        return ResponseEntity.ok(response);
//    }
//}
