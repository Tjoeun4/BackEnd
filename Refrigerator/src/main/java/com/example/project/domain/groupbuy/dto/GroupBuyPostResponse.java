package com.example.project.domain.groupbuy.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class GroupBuyPostResponse {
    private Long postId;
    private String title;
    private String description;
    private int priceTotal;
    private String meetPlaceText;
    private String status;
    private String categoryName;
    private String authorNickname;
    private LocalDateTime createdAt;
    private int currentParticipants; // 현재 참여 인원 (작성자 포함 기본 1명)
    private int maxParticipants; // 기본 모집 정원 15명
    private Double lat; // 위도 (Latitude)
    private Double lng; // 경도 (Longitude)
    
    
 // 추가: 현재 사용자의 찜 여부
    private boolean isFavorite;
}
