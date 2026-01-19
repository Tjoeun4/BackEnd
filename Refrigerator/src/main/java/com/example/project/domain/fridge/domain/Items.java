package com.example.project.domain.fridge.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "items")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Items {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "items_seq")
    @SequenceGenerator(name = "items_seq", sequenceName = "ITEMS_SEQ", allocationSize = 1)
    @Column(name = "item_id")
    private Long id;

    @Column(name = "name", nullable = false, length = 100)
    private String name; 

    @Column(name = "category_id", nullable = false)
    private Long categoryId;

    @Column(name = "expiration_num", nullable = false)
    private Long expirationNum; // 평균 유통기한(일)

    private Items(String name, Long categoryId, Long expirationNum) {
        this.name = name;
        this.categoryId = categoryId;
        this.expirationNum = expirationNum;
    }

    public static Items create(String name, Long categoryId, Long expirationNum) {
        return new Items(name, categoryId, expirationNum);
    }
}
