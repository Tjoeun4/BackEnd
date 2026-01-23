package com.example.project.domain.fridge.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "ITEM_ALIASES") 
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ItemAlias {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "item_aliases_seq")
    @SequenceGenerator(
        name = "item_aliases_seq",
        sequenceName = "ITEM_ALIASES_SEQ",
        allocationSize = 1
    )
    @Column(name = "ITEM_ALIAS_ID")
    private Long itemAliasId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ITEM_ID", nullable = false)
    private Items item;

    @Lob
    @Column(name = "RAW_NAME", nullable = false)
    private String rawName;

    @Column(name = "SOURCE", length = 20, nullable = false)
    private String source;

    public Long getId() {
        return this.itemAliasId;
    }

    public static ItemAlias create(Items item, String rawName, String source) {
        ItemAlias alias = new ItemAlias();
        alias.item = item;
        alias.rawName = rawName;
        alias.source = source;
        return alias;
    }
}
