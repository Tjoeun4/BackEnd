package com.example.project.domain.fridge.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(
    name = "item_aliases",
    uniqueConstraints = {
        @UniqueConstraint(name = "uq_item_aliases_alias", columnNames = "alias")
    }
)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ItemAlias {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "item_aliases_seq")
    @SequenceGenerator(name = "item_aliases_seq", sequenceName = "ITEM_ALIASES_SEQ", allocationSize = 1)
    @Column(name = "item_alias_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id", nullable = false)
    private Items item;

    @Column(name = "alias", nullable = false, length = 200)
    private String alias;

    @Column(name = "source", nullable = false, length = 20)
    private String source; // AI / SYSTEM

    private ItemAlias(Items item, String alias, String source) {
        this.item = item;
        this.alias = alias;
        this.source = source;
    }

    public static ItemAlias create(Items item, String alias, String source) {
        return new ItemAlias(item, alias, source);
    }
}
