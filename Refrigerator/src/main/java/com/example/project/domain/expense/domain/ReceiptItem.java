package com.example.project.domain.expense.domain;

import java.math.BigDecimal;

import jakarta.persistence.*;
import lombok.AccessLevel;
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

    @Column(name = "item_name", nullable = false, length = 120)
    private String itemName;

    @Column(precision = 10, scale = 2)
    private BigDecimal qty;

    private Integer price;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "receipt_id", nullable = false)
    private ReceiptScan receipt;
}
