package com.example.project.domain.fridge.dto;

import lombok.*;

import java.util.List;

@Getter
@AllArgsConstructor
public class IngredientResolveResponse {

    private ResolveType type;

    // type == CONFIRM_ALIAS 일 때 사용
    private AliasCandidate aliasCandidate;

    // type == PICK_ITEM 일 때 사용
    private List<ItemCandidate> itemCandidates;

    // type == AI_PENDING 일 때 사용
    private String message;

    public enum ResolveType {
        CONFIRM_ALIAS, PICK_ITEM, AI_PENDING
    }

    @Getter @AllArgsConstructor
    public static class AliasCandidate {
        private Long itemAliasId;
        private String rawName;
        private Long itemId;
        private String itemName;
    }

    @Getter @AllArgsConstructor
    public static class ItemCandidate {
        private Long itemId;
        private String itemName;
        private Long expirationNum;
    }

    public static IngredientResolveResponse confirmAlias(AliasCandidate c) {
        return new IngredientResolveResponse(ResolveType.CONFIRM_ALIAS, c, null, null);
    }

    public static IngredientResolveResponse pickItem(List<ItemCandidate> list) {
        return new IngredientResolveResponse(ResolveType.PICK_ITEM, null, list, null);
    }

    public static IngredientResolveResponse aiPending(String msg) {
        return new IngredientResolveResponse(ResolveType.AI_PENDING, null, null, msg);
    }
}
