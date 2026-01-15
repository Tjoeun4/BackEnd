package com.example.project.domain.groupbuy.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class GroupBuyPostCreateRequest {
    private String title;
    private String description;
    private int priceTotal;
    private String meetPlaceText;
    private Long categoryId;
    private Long neighborhoodId;
}