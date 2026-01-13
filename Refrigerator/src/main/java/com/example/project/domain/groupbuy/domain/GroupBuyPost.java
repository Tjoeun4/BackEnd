package com.example.project.domain.groupbuy.domain;

import java.util.ArrayList;
import java.util.List;

import com.example.project.domain.fridege.domain.FoodCategory;
import com.example.project.global.controller.BaseTimeEntity;
import com.example.project.member.domain.Users;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "groupbuy_posts")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GroupBuyPost extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long postId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private Users user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private FoodCategory category;

    @Column(nullable = false, length = 120)
    private String title;

    @Column(nullable = false)
    private String description;

    private int priceTotal;
    private String meetPlaceText;
    
    @Column(nullable = false)
    private String status = "OPEN";

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<GroupBuyPostImage> images = new ArrayList<>();
    
    @Column(name = "neighborhood_id", nullable = false)
    private Long neighborhoodId;
}