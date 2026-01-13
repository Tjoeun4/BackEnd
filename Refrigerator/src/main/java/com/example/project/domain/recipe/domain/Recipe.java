package com.example.project.domain.recipe.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "recipes")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Recipe {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long recipeId;

    @Column(nullable = false, length = 120)
    private String name;

    @Column(length = 500)
    private String imageUrl;

    @Lob
    @Column(nullable = false)
    private String instructions;

}
