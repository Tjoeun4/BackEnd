package com.example.project.domain.groupbuy.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import java.time.LocalDateTime;

@Getter
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

}
