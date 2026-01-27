package com.example.project.domain.fridge.domain;

import java.time.LocalDateTime;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "PANTRY_ITEMS")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PantryItem {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PANTRY_ITEMS_SEQ_GEN")
    @SequenceGenerator(name = "PANTRY_ITEMS_SEQ_GEN", sequenceName = "PANTRY_ITEMS_SEQ", allocationSize = 1)
    @Column(name = "PANTRY_ITEM_ID")
    private Long pantryItemId;

    @Column(name = "USER_ID", nullable = false)
    private Long userId;

    @Column(name = "ITEM_NAME", nullable = false, length = 100)
    private String itemName;

    @Column(name = "DELFLAG", nullable = false, length = 1)
    private String delFlag = "N";

    @Column(name = "ENROLLDATE", nullable = false)
    private LocalDateTime enrollDate = LocalDateTime.now();

    public PantryItem(Long userId, String itemName) {
        this.userId = userId;
        this.itemName = itemName;
        this.delFlag = "N";
        this.enrollDate = LocalDateTime.now();
    }

    public void delete() {
        this.delFlag = "Y";
    }

    /** 삭제된 항목 복구 */
    public void restore() {
        this.delFlag = "N";
    }
}
