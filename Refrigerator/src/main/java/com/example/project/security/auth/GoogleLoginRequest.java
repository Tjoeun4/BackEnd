package com.example.project.security.auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GoogleLoginRequest {
    private String idToken;      // 구글 ID 토킹
    private String email;        // 구글 이메일
    private String nickname;
    private String ageRange;     // DB의 age 필드와 매핑 필요
    private String jobCategory;  // DB의 job 필드와 매핑
    private Integer zipCode;
    private String basicAddress;
    private String detailAddress;
}
