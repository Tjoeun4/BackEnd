package com.example.project.domain.fridge.controller;

import com.example.project.domain.fridge.service.RecommendPayloadService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/debug/recommend")
@RequiredArgsConstructor
public class RecommendDebugController {

    private final RecommendPayloadService recommendPayloadService;

    @GetMapping("/payload")
    public Map<String, Object> debugPayload(@RequestParam("userId") Long userId) {
        try {
            return recommendPayloadService.buildPayload(userId);
        } catch (Exception e) {
            Map<String, Object> err = new HashMap<>();
            err.put("error", e.getClass().getName());
            err.put("message", e.getMessage());
            // 원인(cause)까지
            if (e.getCause() != null) {
                err.put("cause", e.getCause().getClass().getName());
                err.put("causeMessage", e.getCause().getMessage());
            }
            return err;
        }
    }
}
