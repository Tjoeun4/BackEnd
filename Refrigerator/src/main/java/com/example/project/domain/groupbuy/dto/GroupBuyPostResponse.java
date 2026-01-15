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
}
