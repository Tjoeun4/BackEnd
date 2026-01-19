package com.example.project.domain.fridge.service;

public interface ItemAiClient {

 AiItemResult infer(String keyword);

 record AiItemResult(
         String normalizedName,
         Long categoryId,
         Long expirationNum
 ) {

 }
}