package com.example.project.security.auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GoogleAuthenticationResponse {
    private String token;
    private boolean isNewUser; // Indicates if the user was newly registered
    private String error; // Optional: for conveying error messages
}
