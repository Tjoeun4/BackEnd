package com.example.project.domain.fridge.dto;

public record AiItemInfo(
    String itemName,
    Integer expirationDays,
    String categoryName,
    Double confidence,
    String notes
) {}
