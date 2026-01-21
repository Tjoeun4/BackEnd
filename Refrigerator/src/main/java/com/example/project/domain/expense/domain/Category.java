package com.example.project.domain.expense.domain;

public enum Category {
	MEAL("식비"),
    INGREDIENT("식재료"),  // 냉장고 연동 핵심 (채소, 고기 등)
    READY_MEAL("완제품/간편식"), // 바로 먹는 것 (김밥, 도시락 등)
    DRINK("주류/음료"),
    TRANSPORT("교통"),
    SHOPPING("쇼핑"),
    LIVING("생활용품"),
    CULTURE("문화/여가"),
    HEALTH("의료/건강"),
    RECEIPT("영수증"), // Expense에서 영수증 통합본임을 나타내는 용도
    ETC("기타");

    private final String description;
    Category(String description) { this.description = description; }
}
