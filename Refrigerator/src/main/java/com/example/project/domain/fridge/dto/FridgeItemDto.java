package com.example.project.domain.fridge.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class FridgeItemDto {

    @Getter
    @NoArgsConstructor
    public static class Request {
        private String rawInputName;

        // 프론트가 match 결과로 itemId를 받았으면 넣어줌(없으면 null)
        private Long matchedItemId;

        // 사용자 확인
        private Boolean confirmed;

        private BigDecimal quantity;
        private String unit;
        private LocalDate purchaseDate; //service에서 null이면 오늘날짜로 설정 
    }

    @Getter
    @AllArgsConstructor
    public static class Response {
        private Long fridgeItemId;
        private Long itemId;
        private String itemName;
        private LocalDate expiryDate;
        private String createdMode; // EXISTING / AI_CREATED
    }
}
