package com.example.project.security.auth;

import com.example.project.member.domain.Users;
import com.example.project.member.repository.UsersRepository;
import com.example.project.security.config.JwtService;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value; // 이 부분을 추가하세요
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class GoogleAuthService {

    @Value("${google.oauth.client-id}")
    private String googleClientId;

    private final UsersRepository usersRepository;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager; // Spring Security AuthenticationManager
    private final PasswordEncoder passwordEncoder;

    public GoogleAuthenticationResponse authenticateGoogleUser(String idToken) throws GeneralSecurityException, IOException {
        GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(new NetHttpTransport(), GsonFactory.getDefaultInstance())
                .setAudience(Collections.singletonList(googleClientId))
                .build();

        GoogleIdToken googleIdToken = verifier.verify(idToken);

        if (googleIdToken != null) {
            GoogleIdToken.Payload payload = googleIdToken.getPayload();
            String email = payload.getEmail();
            String name = (String) payload.get("name");

            Optional<Users> existingUser = usersRepository.findByEmail(email);

            if (existingUser.isEmpty()) {
                // User is new: DO NOT save user yet, just return newUser flag.
                // Frontend will then navigate to GoogleSignUpScreen to collect more info.
                return GoogleAuthenticationResponse.builder()
                        .token(null) // No JWT token yet for new users
                        .newUser(true) // Indicate it's a new user
                        .email(email) // Pass email for signup screen
                        .nickname(name) // Pass nickname for signup screen
                        .build();
            } else {
                // User exists: Authenticate and generate JWT token.
                Users user = existingUser.get();

                // Authenticate the user in Spring Security context
                authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(
                                user.getEmail(),
                                "google_oauth_user_no_password" // Use the raw placeholder password for authentication context
                        )
                );

                String jwtToken = jwtService.generateToken(user);
                return GoogleAuthenticationResponse.builder()
                        .token(jwtToken)
                        .newUser(false) // Indicate it's an existing user
                        .email(email) // Include email for consistency
                        .nickname(name) // Include nickname for consistency
                        .build();
            }

        } else {
            throw new IllegalArgumentException("Invalid Google ID Token");
        }
    }
}