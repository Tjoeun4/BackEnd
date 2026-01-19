package com.example.project.domain.fridge.service;

import com.example.project.domain.fridge.domain.Items;

public interface AiItemService {
    // rawInputName("오뚜기 컵누들") → Items.create("컵누들", categoryId, expirationNum)
    Items createItemFromAi(String rawInputName);
}
