package com.example.project.security.auth;

import com.example.project.global.neighborhood.Neighborhood;
import com.example.project.global.neighborhood.NeighborhoodRepository;
import com.example.project.member.domain.Users;
import com.example.project.member.repository.UsersRepository;
import com.example.project.security.config.JwtService;
import com.example.project.security.token.Token;
import com.example.project.security.token.TokenRepository;
import com.example.project.security.token.TokenType;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UsersRepository repository;
    private final TokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final RestTemplate restTemplate; // RestTemplate을 빈으로 주입받아 사용
    private final NeighborhoodRepository neighborhoodRepository;

    /**
     * 회원가입 처리
     */
    public AuthenticationResponse register(RegisterRequest request) {

        // 전달받은 neighborhoodId로 동네 정보 조회
        Optional<Neighborhood> neighborhood1 =
                neighborhoodRepository.findById(request.getNeighborhoodId());

        // 회원 엔티티 생성
        var user = Users.builder()
                .nickname(request.getNickname())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .gender(request.getGender())
                .age(request.getAge())
                .zipCode(request.getZipcode())
                .addressBase(request.getAddressBase())
                .addressDetail(request.getAddressDetail())
                .monthlyFoodBudget(request.getMonthlyFoodBudget())
                .neighborhood(neighborhood1.get())
                .build();

        try {
            // 사용자 저장 (여기서 유니크 제약조건 위반 시 예외 발생 가능)
            var savedUser = repository.save(user);

            // JWT 토큰 및 리프레시 토큰 생성
            var jwtToken = jwtService.generateToken(user);
            var refreshToken = jwtService.generateRefreshToken(user);

            // 사용자 토큰 저장
            saveUserToken(savedUser, jwtToken);

            return AuthenticationResponse.builder()
                    .accessToken(jwtToken)
                    .refreshToken(refreshToken)
                    .userId(user.getUserId()) // DB에 저장된 user의 ID 반환
                    .build();

        } catch (DataIntegrityViolationException e) {

            // DB 예외의 실제 원인이 SQL 예외인지 확인 (Oracle ORA-00001 등)
            Throwable rootCause = e.getRootCause();

            if (rootCause instanceof SQLException) {
                SQLException sqlEx = (SQLException) rootCause;

                // Oracle의 UNIQUE 제약조건 위반 에러 코드는 1
                if (sqlEx.getErrorCode() == 1) {

                    // 에러 메시지에 EMAIL 관련 제약조건이 포함된 경우
                    // (예: SPRINGBOOT.SYS_C008889 → 이메일 유니크 제약)
                    if (sqlEx.getMessage() != null && sqlEx.getMessage().contains("EMAIL")) {
                        throw new DuplicateEmailException("이미 가입된 이메일 주소입니다.");
                    }
                }
            }

            // 이메일 중복이 아닌 다른 무결성 오류라면 그대로 예외 재발생
            throw e;
        }
    }

    /**
     * 일반 로그인 처리 (이메일 + 비밀번호)
     */
    public AuthenticationResponse authenticate(AuthenticationRequest request) {

        // Spring Security 인증 처리
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        // 인증 성공 시 사용자 조회
        var user = repository.findByEmail(request.getEmail())
                .orElseThrow();

        // JWT 및 Refresh Token 생성
        var jwtToken = jwtService.generateToken(user);
        var refreshToken = jwtService.generateRefreshToken(user);

        // 기존 토큰 모두 폐기
        revokeAllUserTokens(user);

        // 새 토큰 저장
        saveUserToken(user, jwtToken);

        return AuthenticationResponse.builder()
                .accessToken(jwtToken)
                .refreshToken(refreshToken)
                .userId(user.getUserId()) // 로그인 성공한 사용자 ID 반환
                .build();
    }

    /**
     * 사용자 토큰 저장
     */
    private void saveUserToken(Users user, String jwtToken) {
        var token = Token.builder()
                .user(user)
                .token(jwtToken)
                .tokenType(TokenType.BEARER)
                .expired(false)
                .revoked(false)
                .build();
        tokenRepository.save(token);
    }

    /**
     * 해당 사용자의 모든 기존 토큰을 만료 처리
     */
    private void revokeAllUserTokens(Users user) {
        var validUserTokens =
                tokenRepository.findAllValidTokenByUser(user.getUserId());

        if (validUserTokens.isEmpty()) return;

        validUserTokens.forEach(token -> {
            token.setExpired(true);
            token.setRevoked(true);
        });

        tokenRepository.saveAll(validUserTokens);
    }

    /**
     * Refresh Token을 이용해 Access Token 재발급
     */
    public void refreshToken(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws IOException {

        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        final String refreshToken;
        final String userEmail;

        // Authorization 헤더가 없거나 Bearer 토큰이 아니면 종료
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return;
        }

        refreshToken = authHeader.substring(7);
        userEmail = jwtService.extractUsername(refreshToken);

        if (userEmail != null) {
            var user = repository.findByEmail(userEmail)
                    .orElseThrow();

            // Refresh Token 유효성 검증
            if (jwtService.isTokenValid(refreshToken, user)) {

                // 새로운 Access Token 발급
                var accessToken = jwtService.generateToken(user);

                // 기존 토큰 폐기 후 새 토큰 저장
                revokeAllUserTokens(user);
                saveUserToken(user, accessToken);

                var authResponse = AuthenticationResponse.builder()
                        .accessToken(accessToken)
                        .refreshToken(refreshToken)
                        .build();

                // 응답으로 토큰 반환
                new ObjectMapper().writeValue(
                        response.getOutputStream(),
                        authResponse
                );
            }
        }
    }

    /**
     * 공통 SNS 로그인 처리 (구글, 카카오 등)
     */
    public AuthenticationResponse socialLogin(SocialLoginRequest request) {

        // 1. (선택사항)
        // idToken을 사용해 구글/카카오 서버에 토큰 유효성 검증을 추가할 수 있음
        // 현재는 클라이언트가 전달한 정보를 신뢰하는 구조

        // 2. 이메일 기준으로 사용자 조회, 없으면 신규 회원 생성
        var user = repository.findByEmail(request.getEmail())
                .orElseGet(() -> {
                    Users newUser = Users.builder()
                            .email(request.getEmail())
                            .nickname(request.getNickname())
                            // Users 엔티티 컬럼 구조에 맞게 수정 필요
                            //.age(request.getAgeRange())
                            .zipCode(request.getZipCode())
                            .address(
                                    request.getBasicAddress()
                                            + " "
                                            + request.getDetailAddress()
                            )
                            .build();
                    return repository.save(newUser);
                });

        // 3. 기존 JWT 발급 로직 재사용
        var jwtToken = jwtService.generateToken(user);
        var refreshToken = jwtService.generateRefreshToken(user);

        // 기존 토큰 만료 처리 후 새 토큰 저장
        revokeAllUserTokens(user);
        saveUserToken(user, jwtToken);

        return AuthenticationResponse.builder()
                .accessToken(jwtToken)
                .refreshToken(refreshToken)
                .userId(user.getUserId())
                .build();
    }

    /**
     * 카카오 사용자 정보 조회
     */
    private SocialUserInfo getKakaoUserInfo(String token) {

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);

        ResponseEntity<Map> response = restTemplate.exchange(
                "https://kapi.kakao.com/v2/user/me",
                HttpMethod.GET,
                new HttpEntity<>(headers),
                Map.class
        );

        Map<String, Object> body = response.getBody();
        Map<String, Object> kakaoAccount =
                (Map<String, Object>) body.get("kakao_account");
        Map<String, Object> profile =
                (Map<String, Object>) kakaoAccount.get("profile");

        return new SocialUserInfo(
                (String) kakaoAccount.get("email"),
                (String) profile.get("nickname")
        );
    }

    /**
     * 구글 사용자 정보 조회
     */
    private SocialUserInfo getGoogleUserInfo(String token) {

        RestTemplate restTemplate = new RestTemplate();

        // 구글 사용자 정보 조회 엔드포인트
        String url =
                "https://www.googleapis.com/oauth2/v3/userinfo?access_token=" + token;

        ResponseEntity<Map> response =
                restTemplate.getForEntity(url, Map.class);

        Map<String, Object> body = response.getBody();

        return new SocialUserInfo(
                (String) body.get("email"),
                (String) body.get("name")
        );
    }

    /**
     * SNS 로그인 사용자 정보를 담기 위한 내부 헬퍼 클래스
     */
    @lombok.Getter
    @lombok.AllArgsConstructor
    private static class SocialUserInfo {
        private String email;
        private String nickname;
    }
}
