package com.example.project.domain.expense.domain;

import java.time.LocalDateTime;

import com.example.project.global.controller.BaseTimeEntity;
import com.example.project.member.domain.Users;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "expenses")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Expense extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long expenseId;

    @Column(nullable = false)
    private LocalDateTime spentAt;

    @Column(nullable = false)
    private Long amount;

    private String title;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Category category = Category.ETC; // 기본값은 ETC

    @Lob
    @Column(name = "memo")
    private String memo; // AI가 요약한 영수증 아이템 리스트 (01..., 02...)

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true) // 👈 orphanRemoval 추가
    @JoinColumn(name = "receipt_id")
    private ReceiptScan receiptScan;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private Users user;

    @Builder
    public Expense(LocalDateTime spentAt, Long amount, String title, Category category, String memo, ReceiptScan receiptScan, Users user) {
        this.spentAt = (spentAt != null) ? spentAt : LocalDateTime.now();
        this.amount = amount;
        this.title = title;
        // 빌더에서 카테고리를 명시하지 않으면 ETC를 기본값으로 사용
        this.category = (category != null) ? category : Category.ETC; 
        this.memo = memo;
        this.receiptScan = receiptScan;
        this.user = user;
    }

    // 서비스 레이어에서 사용할 update 메서드 추가
    public void update(LocalDateTime spentAt, Long amount, String title, Category category, String memo) {
        if (spentAt != null) this.spentAt = spentAt;
        if (amount != null) this.amount = amount;
        if (title != null) this.title = title;
        if (category != null) this.category = category;
        this.memo = memo;
    }
}