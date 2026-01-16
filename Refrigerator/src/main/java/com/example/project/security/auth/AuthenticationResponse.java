package com.example.project.security.auth;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationResponse {

    @JsonProperty("access_token")
    private String accessToken;
    @JsonProperty("refresh_token")
    private String refreshToken;

    //[추가] 프론트엔드로 보낼 사용자 ID
    @JsonProperty("user_id")
    private Long userId;

    @JsonProperty("onboarding_survey_completed")
    private Boolean onboardingSurveyCompleted; // 온보딩 설문 완료 여부

    private String error;
}
