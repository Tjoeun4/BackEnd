package com.example.project.domain.fridege.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.project.domain.fridege.domain.FoodCategory;
import com.example.project.member.domain.Users;

public interface FoodCategoryRepository extends JpaRepository<FoodCategory, Long> {

	Optional<FoodCategory> findByCategoryId(Long categoryId);
	
}
