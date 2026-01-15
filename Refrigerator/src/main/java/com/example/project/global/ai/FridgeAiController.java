package com.example.project.global.ai;

import java.util.Map;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.project.domain.fridege.domain.FridgeAiPayloadService;
import com.example.project.domain.fridege.domain.FridgeAiPrompt;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/test/ai")
@RequiredArgsConstructor
public class FridgeAiController {

    private final FridgeAiPayloadService payloadService;
    private final GeminiClient geminiClient;

    @PostMapping("/fridge/{userId}")
    public Map<String, Object> test(@PathVariable("userId") String userId) {

        // ✅ 여기서 직접 숫자 변환 (팀 복호화 로직 우회)
        Long id = Long.parseLong(userId);

        System.out.println("✅ FridgeAiController HIT userId=" + id);

        Map<String, Object> payload = payloadService.buildPayload(id);
        String prompt = FridgeAiPrompt.build(payload);
        return geminiClient.generate(prompt);
    }
}
