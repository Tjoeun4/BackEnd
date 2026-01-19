package com.example.project.domain.fridge.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "food_categories")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FoodCategory {

	@Id
	@Column(name="category_id")
	private Long categoryId;


	@Column(name="name", nullable=false, length=50, unique=true)
	private String name;
    

    public static FoodCategory create(Long id, String name) {
        FoodCategory c = new FoodCategory();
        c.categoryId = id;
        c.name = name;
        return c;
    }
}
