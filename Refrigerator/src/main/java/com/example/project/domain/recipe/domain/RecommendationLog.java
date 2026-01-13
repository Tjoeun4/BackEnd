package com.example.project.domain.recipe.domain;

import com.example.project.global.controller.BaseTimeEntity;
import com.example.project.member.domain.Users;
import com.example.project.domain.recipe.domain.Recipe;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "recommendation_logs")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RecommendationLog extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long logId;

    @Lob
    @Column(name = "input_snapshot", nullable = false)
    private String inputSnapshot;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private Users user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recipe_id", nullable = false)
    private Recipe recipe;
}
