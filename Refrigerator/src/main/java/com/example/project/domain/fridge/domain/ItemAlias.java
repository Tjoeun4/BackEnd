package com.example.project.domain.fridge.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.*;

@Entity
@Table(
    name = "item_aliases",
    uniqueConstraints = {
        @UniqueConstraint(name = "uq_item_aliases_raw_name", columnNames = {"raw_name"})
    }
)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ItemAlias {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "item_aliases_seq")
    @SequenceGenerator(name = "item_aliases_seq", sequenceName = "ITEM_ALIASES_SEQ", allocationSize = 1)
    @Column(name = "item_alias_id")
    private Long itemAliasId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "item_id", nullable = false)
    private Items item;

    @Lob
    @Column(name = "raw_name", nullable = false)
    private String rawName;

    @Column(name = "source", length = 20, nullable = false)
    private String source;

    @Builder
    private ItemAlias(Items item, String rawName, String source) {
        this.item = item;
        this.rawName = rawName;
        this.source = source;
    }

    public static ItemAlias userProvided(Items item, String rawName) {
        return ItemAlias.builder()
            .item(item)
            .rawName(rawName)
            .source("USER")
            .build();
    }
}
