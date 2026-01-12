package com.example.project.domain.fridege.domain;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.example.project.global.controller.BaseTimeEntity;
import com.example.project.member.domain.Users;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "fridge_items")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FridgeItem extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long fridgeItemId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private Users user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private FoodCategory category;

    @Column(nullable = false, length = 100)
    private String name;

    private BigDecimal quantity;
    private String unit;
    private LocalDate purchaseDate;
    private LocalDate expiryDate;

    @Column(nullable = false, length = 20)
    private String status = "ACTIVE";
}
