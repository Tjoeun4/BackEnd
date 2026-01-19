package com.example.project.domain.fridge.service;

import org.springframework.stereotype.Service;

import com.example.project.domain.fridge.domain.Items;
import com.example.project.domain.fridge.repository.FoodCategoryRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AiItemServiceImpl implements AiItemService {

  private final ItemAiClient itemAiClient;        // GeminiAiClient가 여기로 주입됨

  @Override
  public Items createItemFromAi(String rawInputName) {
     ItemAiClient.AiItemResult r = itemAiClient.infer(rawInputName);
     return Items.create(r.normalizedName(), r.categoryId(), r.expirationNum());
  }
}
