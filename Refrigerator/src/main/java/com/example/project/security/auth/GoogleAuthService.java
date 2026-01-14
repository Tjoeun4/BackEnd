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

    public String authenticateGoogleUser(String idToken) throws GeneralSecurityException, IOException {
        GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(new NetHttpTransport(), GsonFactory.getDefaultInstance())
                .setAudience(Collections.singletonList(googleClientId))
                .build();

        GoogleIdToken googleIdToken = verifier.verify(idToken);

        if (googleIdToken != null) {
            GoogleIdToken.Payload payload = googleIdToken.getPayload();
            String email = payload.getEmail();
            String name = (String) payload.get("name");
            // String picture = (String) payload.get("picture"); // profilePicture not in Users entity

            Optional<Users> existingUser = usersRepository.findByEmail(email);
            Users user;
            if (existingUser.isEmpty()) {
                // Register new user
                user = Users.builder()
                        .email(email)
                        .nickname(name != null ? name : email) // Use name if available, else email
                        .password(passwordEncoder.encode("google_oauth_user_no_password")) // Encode placeholder password
                        // You might want to set a default password or handle it differently for Google users
                        .build();
                usersRepository.save(user);
            } else {
                user = existingUser.get();
                // Optionally update user details if needed
            }

            // Authenticate the user in Spring Security context
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            user.getEmail(),
                            "google_oauth_user_no_password" // Use the raw placeholder password for authentication context
                    )
            );

            return jwtService.generateToken(user);

        } else {
            throw new IllegalArgumentException("Invalid Google ID Token");
        }
    }
}