package com.example.project.domain.groupbuy.domain;

import java.util.ArrayList;
import java.util.List;

import com.example.project.domain.fridege.domain.FoodCategory;
import com.example.project.global.controller.BaseTimeEntity;
import com.example.project.global.neighborhood.Neighborhood;
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
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "groupbuy_posts")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor // Builder 사용을 위해 추가
@Builder // Builder 패턴 활성화
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

    private String description;

    private int priceTotal;
    private String meetPlaceText;
    
    @Column(nullable = false)
    private String status = "OPEN";

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<GroupBuyPostImage> images = new ArrayList<>();
    
 // GroupBuyPost.java 수정
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "neighborhood_id", nullable = false)
    private Neighborhood neighborhood;

 // GroupBuyPost.java 내부에 추가
    @Builder.Default
    @Column(nullable = false)
    private int maxParticipants = 15; // 기본 모집 정원 15명

    @Builder.Default
    @Column(nullable = false)
    private int currentParticipants = 1; // 현재 참여 인원 (작성자 포함 기본 1명)

    // 인원 증가 메서드
    public void incrementParticipants() {
        this.currentParticipants++;
    }

    // 상태 변경 메서드
    public void updateStatus(String status) {
        this.status = status;
    }
    
    
}