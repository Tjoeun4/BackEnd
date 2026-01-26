package com.example.project.domain.fridge.domain;

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
import jakarta.persistence.SequenceGenerator;
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
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "fridge_items_seq")
    @SequenceGenerator(name = "fridge_items_seq", sequenceName = "FRIDGE_ITEMS_SEQ", allocationSize = 1)
    @Column(name = "fridge_item_id")
    private Long fridgeItemId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private Users user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id", nullable = true)
    private Items item;

    @Column(name = "raw_name", nullable = false, columnDefinition = "CLOB")
    private String rawName;
    
    @Column(name = "quantity", precision = 10, scale = 2)
    private BigDecimal quantity;

    @Column(name = "unit", length = 20)
    private String unit;

    @Column(name = "purchase_date")
    private LocalDate purchaseDate;

    @Column(name = "expiry_date")
    private LocalDate expiryDate;

    @Column(name = "consume_date")
    private LocalDate consumeDate;

    @Column(name = "status", nullable = false, length = 20)
    private String status = "ACTIVE";

    public static FridgeItem create(
            Users user,
            Items item,
            String rawName,
            BigDecimal quantity,
            String unit,
            LocalDate purchaseDate,
            LocalDate expiryDate
            
    ) {
        FridgeItem fi = new FridgeItem();
        fi.user = user;
        fi.item = item;
        fi.rawName = rawName;
        fi.quantity = quantity;
        fi.unit = unit;
        fi.purchaseDate = purchaseDate;
        fi.expiryDate = expiryDate;
        fi.status = "ACTIVE";
        return fi;
    }
    
    public static FridgeItem make(
            Users user,
            String rawName,
            BigDecimal quantity,
            String unit,
            LocalDate purchaseDate,
            LocalDate expiryDate
            
    ) {
        FridgeItem fi = new FridgeItem();
        fi.user = user;
        fi.rawName = rawName;
        fi.quantity = quantity;
        fi.unit = unit;
        fi.purchaseDate = purchaseDate;
        fi.expiryDate = expiryDate;
        fi.status = "ACTIVE";
        return fi;
    }

    /** 소프트 삭제: status를 REMOVED로 변경 (목록 조회 제외) */
    public void markAsRemoved() {
        this.status = "REMOVED";
    }

}
