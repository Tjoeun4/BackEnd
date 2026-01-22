package com.example.project.domain.fridge.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
public class IngredientCreateResponse {
    private Long fridgeItemId;
    private Long itemId;
    private String itemName;
    private String rawName;
    private LocalDate purchaseDate;
    private LocalDate expiryDate;
}
