package com.example.project.domain.fridege.domain;

import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Service
public class FridgeAiPayloadService {

    private final FridgeItemRepository fridgeItemRepository;

    public FridgeAiPayloadService(FridgeItemRepository fridgeItemRepository) {
        this.fridgeItemRepository = fridgeItemRepository;
    }

    public Map<String, Object> buildPayload(Long userId) {
        List<FridgeItem> items = fridgeItemRepository.findActiveByUserOrderByExpiry(userId);

        LocalDate today = LocalDate.now();

        List<Map<String, Object>> fridge = new ArrayList<>();
        for (FridgeItem i : items) {
            Integer expiryDays = null;
            if (i.getExpiryDate() != null) {
                expiryDays = (int) ChronoUnit.DAYS.between(today, i.getExpiryDate());
            }

            String qty = toQtyString(i.getQuantity(), i.getUnit());

            fridge.add(Map.of(
                    "item", normalize(i.getName()),
                    "qty", qty,
                    "expiry_days", expiryDays
            ));
        }

        // 기본 조미료는 항상 있다고 가정 (추가 구매 아님)
        List<String> pantry = List.of(
                "salt",
                "pepper",
                "soy_sauce",
                "garlic",
                "sesame_oil",
                "sugar"
        );

        return Map.of(
                "servings", 2,
                "time_limit_min", 20,
                "pantry", pantry,
                "fridge", fridge
        );
    }

    private String normalize(String s) {
        if (s == null) return "";
        return s.trim().toLowerCase().replaceAll("\\s+", " ");
    }

    private String toQtyString(java.math.BigDecimal quantity, String unit) {
        if (quantity == null && (unit == null || unit.isBlank())) return null;
        if (quantity == null) return unit;
        if (unit == null || unit.isBlank()) return quantity.toPlainString();
        return quantity.toPlainString() + unit;
    }
}