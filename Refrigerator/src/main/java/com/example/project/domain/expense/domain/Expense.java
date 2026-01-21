package com.example.project.domain.expense.domain;

import java.time.LocalDateTime;

import com.example.project.global.controller.BaseTimeEntity;
import com.example.project.member.domain.Users;

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
    private int amount = 0;

    private String title; // null 허용

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Category category = Category.ETC;

    @Column(length = 1000)
    private String memo; // null 허용

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private Users user;

    @Builder
    public Expense(LocalDateTime spentAt, Integer amount, String title, Category category, String memo, Users user) {
        this.spentAt = (spentAt != null) ? spentAt : LocalDateTime.now();
        this.amount = (amount != null) ? amount : 0;
        this.title = title;
        this.category = (category != null) ? category : Category.ETC;
        this.memo = memo;
        this.user = user;
    }

    public void update(LocalDateTime spentAt, Integer amount, String title, Category category, String memo) {
        if (spentAt != null) this.spentAt = spentAt;
        if (amount != null) this.amount = amount;
        this.title = title;
        this.category = (category != null) ? category : Category.ETC;
        this.memo = memo;
    }
}