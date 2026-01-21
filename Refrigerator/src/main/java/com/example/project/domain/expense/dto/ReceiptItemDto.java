package com.example.project.domain.expense.dto;

import com.example.project.domain.expense.domain.Category;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReceiptItemDto {
	private String itemName;      // 상품명
    private Long unitAmount;       // 단가
    private Long quantity;         // 구매 수량(개수)
    private String amount;         // [추가] 실제 양 (예: "500g", "1.5L")
    private Long price;            // 총액
    private Category category;     // 카테고리 (Enum)
    private Boolean isFridgeTarget; // 냉장고 연동 여부
}
