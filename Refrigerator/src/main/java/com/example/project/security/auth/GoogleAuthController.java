package com.example.project.security.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.security.GeneralSecurityException;

@RestController
@RequestMapping("/api/auth/google")
@RequiredArgsConstructor
public class GoogleAuthController {

    private final GoogleAuthService googleAuthService;

    @PostMapping("/signin")
    public ResponseEntity<GoogleAuthenticationResponse> googleSignIn(@RequestBody GoogleLoginRequest request) {
        try {
            GoogleAuthenticationResponse authResponse = googleAuthService.authenticateGoogleUser(request.getIdToken());
            return ResponseEntity.ok(authResponse);
        } catch (GeneralSecurityException | IOException | IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(GoogleAuthenticationResponse.builder().token(null).error(e.getMessage()).build());
        }
    }
}
