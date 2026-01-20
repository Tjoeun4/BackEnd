package com.example.project.domain.expense.domain;

public enum Category {
    MEAL("식비"),
    TRANSPORT("교통비"),
    SHOPPING("쇼핑"),
    INGREDIENT("재료비"), // 냉장고 연동을 위한 핵심 항목
    LIVING("생활용품"),
    ETC("기타"); // 기본값

    private final String description;
    Category(String description) { this.description = description; }
}
