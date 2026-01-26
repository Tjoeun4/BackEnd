package com.example.project.domain.expense.dto;

import com.example.project.domain.expense.domain.Category;
import com.fasterxml.jackson.annotation.JsonFormat; // JSON 날짜 파싱용
import java.time.LocalDate; // 날짜 타입

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReceiptItemDto {
    private String itemName;       // 상품명
    private Long unitAmount;       // 단가
    private Long quantity;         // 구매 수량(개수)
    private String amount;         // 실제 양 (예: "500g", "1.5L")
    private Long price;            // 총액
    private Category category;     // 카테고리 (Enum)
    private Boolean isFridgeTarget; // 냉장고 연동 여부
    private String subCategory;    // 상세 분류 (야채, 육류 등)

    // [추가된 필드]
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate sellByDate;  // 유통기한 (영수증 표기 or null)

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate useByDate;   // 소비기한 (영수증 표기 or null or 자동생성)
}