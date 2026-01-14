package com.example.project.security.auth;

import com.example.project.member.domain.Users;
import com.example.project.member.repository.UsersRepository;
import com.example.project.security.config.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
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
    private final UsersRepository usersRepository; // Inject UsersRepository
    private final PasswordEncoder passwordEncoder; // Inject PasswordEncoder
    private final JwtService jwtService; // Inject JwtService
    private final AuthenticationManager authenticationManager; // Already injected

    @PostMapping("/signin")
    public ResponseEntity<GoogleAuthenticationResponse> googleSignIn(@RequestBody GoogleLoginRequest request) {
        try {
            GoogleAuthenticationResponse authResponse = googleAuthService.authenticateGoogleUser(request.getIdToken());
            return ResponseEntity.ok(authResponse);
        } catch (GeneralSecurityException | IOException | IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(GoogleAuthenticationResponse.builder().token(null).error(e.getMessage()).build());
        }
    }

    @PostMapping("/register-complete")
    public ResponseEntity<GoogleAuthenticationResponse> completeGoogleRegistration(@RequestBody GoogleRegisterRequest request) {
        try {
            // Check if email already exists to prevent duplicate registration in this final step
            if (usersRepository.findByEmail(request.getEmail()).isPresent()) {
                throw new IllegalArgumentException("Email already registered.");
            }

            Users user = Users.builder()
                    .email(request.getEmail())
                    .nickname(request.getNickname() != null ? request.getNickname() : request.getEmail())
                    .password(passwordEncoder.encode("google_oauth_user_no_password")) // Encode placeholder password
                    .gender(request.getGender())
                    .age(request.getAge())
                    .zipCode(request.getZipcode())
                    .addressBase(request.getAddressBase())
                    .addressDetail(request.getAddressDetail())
                    .monthlyFoodBudget(request.getMonthlyFoodBudget())
                    .build();
            usersRepository.save(user);

            // Authenticate the newly registered user
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            user.getEmail(),
                            "google_oauth_user_no_password"
                    )
            );

            String jwtToken = jwtService.generateToken(user);
            return ResponseEntity.ok(
                    GoogleAuthenticationResponse.builder()
                            .token(jwtToken)
                            .newUser(false) // 회원가입 완료
                            .build()
            );

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(GoogleAuthenticationResponse.builder().token(null).error(e.getMessage()).build());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(GoogleAuthenticationResponse.builder().token(null).error("Registration failed: " + e.getMessage()).build());
        }
    }
}
