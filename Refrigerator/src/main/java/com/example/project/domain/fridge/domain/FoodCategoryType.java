package com.example.project.domain.fridge.domain;

public enum FoodCategoryType {
    MEAT(1L, "육류"),
    SEASONING(2L, "양념"),
    VEGETABLE(3L, "채소"),
    DAIRY(4L, "유제품"),
    SEAFOOD(5L, "해산물"),
    FRUIT(6L, "과일");

    private final Long id;
    private final String label;

    FoodCategoryType(Long id, String label) {
        this.id = id;
        this.label = label;
    }

    public Long id() { return id; }
    public String label() { return label; }

    public static FoodCategoryType fromLabel(String label) {
        if (label == null) return null;
        String s = label.trim();
        for (FoodCategoryType t : values()) {
            if (t.label.equals(s)) return t;
        }
        return null; // 못 찾으면 null
    }
}
