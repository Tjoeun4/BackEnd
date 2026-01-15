package com.example.project.domain.fridege.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.project.domain.fridege.domain.FoodCategory;

public interface FoodCategoryRepository extends JpaRepository<FoodCategory, Long> {

}
