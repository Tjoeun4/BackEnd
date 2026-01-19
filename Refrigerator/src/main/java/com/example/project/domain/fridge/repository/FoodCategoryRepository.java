package com.example.project.domain.fridge.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.project.domain.fridge.domain.FoodCategory;
import java.util.Optional;

public interface FoodCategoryRepository extends JpaRepository<FoodCategory, Long> {
    Optional<FoodCategory> findByName(String name);
    boolean existsByName(String name);
}
