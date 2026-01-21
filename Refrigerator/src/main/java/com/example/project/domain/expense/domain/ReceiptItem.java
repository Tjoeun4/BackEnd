package com.example.project.domain.expense.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;



@Entity
@Table(name = "receipt_items")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ReceiptItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 200)
    private String itemName; // 풀네임 (예: 진로골드소주)

 // 엔티티에도 동일하게 필드 추가
    private String amount;// 용량/단위 (예: 360ml, 500g)
    private Long unitAmount; // 단가

    private Long quantity; // 수량 (예: 3)

    private Long price; // 최종 지출 금액

    @Enumerated(EnumType.STRING)
    private Category category; // 아이템별 카테고리 (DRINK, INGREDIENT 등)

    @Column(nullable = false)
    private boolean isFridgeTarget; // 냉장고 연동 가능 여부 (AI 판단)

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "receipt_id", nullable = false)
    private ReceiptScan receipt;

    @Builder
    public ReceiptItem(String itemName, String amount, Long unitAmount, Long quantity, Long price, Category category, boolean isFridgeTarget, ReceiptScan receipt) {
        this.itemName = itemName;
        this.unitAmount = unitAmount;
        this.quantity = quantity;
        this.price = price;
        this.category = category;
        this.isFridgeTarget = isFridgeTarget;
        this.receipt = receipt;
        this.amount = amount; // 생성자에 추가되어야 빌더에서 .amount() 사용 가능
    }
}
