package com.example.project.member.dto;

import com.example.project.member.domain.Users;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserResponseDto {
    private Long userId;
    private String email;
    private String nickname;
    private String profileImageUrl;
    private Boolean onboardingSurveyCompleted;
    private String address;
    private Integer monthlyFoodBudget;
    
    // Neighborhood 정보 추가
    private Long neighborhoodId;
    private String neighborhoodCityName;
    private String neighborhoodDisplayName;
    private String zipCode;

    // 엔티티를 DTO로 변환하는 정적 메서드 (팩토리 메서드 패턴)
    public static UserResponseDto from(Users user) {
        return UserResponseDto.builder()
                .userId(user.getUserId())
                .email(user.getEmail())
                .nickname(user.getNickname() != null ? user.getNickname() : "")
                .profileImageUrl(user.getProfileImage() != null ? user.getProfileImage().getImageUrl() : "")
                .onboardingSurveyCompleted(user.getOnboardingSurveyCompleted())
                .address(user.getAddress() != null ? user.getAddress() : "")
                .monthlyFoodBudget(user.getMonthlyFoodBudget() != null ? user.getMonthlyFoodBudget() : 0)
                .neighborhoodId(user.getNeighborhood() != null ? user.getNeighborhood().getNeighborhoodId() : null)
                .neighborhoodCityName(user.getNeighborhood() != null ? user.getNeighborhood().getCityName() : null)
                .neighborhoodDisplayName(user.getNeighborhood() != null ? user.getNeighborhood().getDisplayName() : null)
                .zipCode(user.getZipCode() != null ? user.getZipCode() : null)
                .build();
    }
}
