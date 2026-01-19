package com.example.project.domain.fridge.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class ItemMatchDto {

    @Getter
    @NoArgsConstructor
    public static class Request {
        private String rawInputName;
    }

    @Getter
    @AllArgsConstructor
    public static class Response {
        private boolean matched;
        private Long itemId;        
        private String itemName;    
        private Long categoryId;
        private Long expirationNum; // 평균 유통기한 일수
        private String matchedBy;   // ALIAS / NAME / NONE
    }
}
