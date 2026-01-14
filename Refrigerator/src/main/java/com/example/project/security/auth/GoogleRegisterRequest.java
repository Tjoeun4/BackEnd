package com.example.project.security.auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GoogleRegisterRequest {
    private String email;
    private String nickname;
    private String gender; // Added
    private Integer age; // Added
    private String zipcode; // Added
    private String addressBase; // Added
    private String addressDetail; // Added
    private Integer monthlyFoodBudget; // Added
}
