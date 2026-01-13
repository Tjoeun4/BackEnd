package com.example.project.security.auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SocialLoginRequest {
    private String idToken;      // SNS에서 받은 토큰
    private String provider;     // "GOOGLE", "KAKAO" 등
    
    // 추가 정보 필드들
    private String email;
    private String password;     // SNS 유저라도 내부 관리를 위해 필요할 수 있음
    private String nickname;
    private String ageRange;
    private String jobCategory;
    private Integer zipCode;
    private String basicAddress;
    private String detailAddress;
}
