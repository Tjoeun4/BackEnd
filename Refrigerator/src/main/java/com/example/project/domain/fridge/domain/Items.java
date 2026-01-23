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
    private Long itemId;

    @Column(name = "name", length = 100, nullable = false)
    private String name;

    @Column(name = "category_id")
    private Long categoryId;

    @Column(name = "expiration_num")
    private Long expirationNum;

    // ✅ 호환용: 서비스에서 getId()로 통일해서 쓰려고 추가
    public Long getId() {
        return this.itemId;
    }

    // ✅ 호환용: 서비스에서 Items.create(...)로 통일해서 쓰려고 추가
    public static Items create(String name, Long categoryId, Long expirationNum) {
        Items i = new Items();
        i.name = name;
        i.categoryId = categoryId;
        i.expirationNum = expirationNum;
        return i;
    }
}
