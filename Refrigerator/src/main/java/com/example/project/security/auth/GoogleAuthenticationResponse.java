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
    private boolean newUser; // Indicates if the user was newly registered (renamed from isNewUser)
    private String email; // Added for new user registration flow
    private String nickname; // Added for new user registration flow
    private String error; // Optional: for conveying error messages
}
