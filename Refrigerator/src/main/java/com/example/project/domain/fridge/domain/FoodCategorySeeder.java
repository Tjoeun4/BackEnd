package com.example.project.domain.fridge.domain;


import java.util.List;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.example.project.domain.fridge.repository.FoodCategoryRepository;
import com.example.project.domain.fridge.domain.FoodCategory;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class FoodCategorySeeder implements ApplicationRunner {

 private final FoodCategoryRepository foodCategoryRepository;

 // 고정 카테고리
 private static final List<CategorySeed> SEEDS = List.of(
         new CategorySeed(1L, "육류"),
         new CategorySeed(2L, "양념"),
         new CategorySeed(3L, "채소"),
         new CategorySeed(4L, "유제품"),
         new CategorySeed(5L, "해산물"),
         new CategorySeed(6L, "과일")
 );

 @Override
 @Transactional
 public void run(ApplicationArguments args) {
     for (CategorySeed seed : SEEDS) {
         if (foodCategoryRepository.existsById(seed.id())) {
             continue;
         }


         FoodCategory category = FoodCategory.create(seed.id(), seed.name());
         foodCategoryRepository.save(category);
     }
 }

 private record CategorySeed(Long id, String name) {}
}
