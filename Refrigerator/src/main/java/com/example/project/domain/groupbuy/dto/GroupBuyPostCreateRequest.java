package com.example.project.domain.groupbuy.dto;

import java.time.LocalDate;

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
    private LocalDate startdate;
    private LocalDate enddate;
    private Double lat; // 위도 (Latitude)
    private Double lng; // 경도 (Longitude)
    
    
    
    
}