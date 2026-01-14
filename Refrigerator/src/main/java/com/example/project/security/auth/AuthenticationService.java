package com.example.project.security.auth;

import java.io.IOException;
import java.util.Map;
import java.util.UUID;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.example.project.member.domain.Users;
import com.example.project.member.repository.UsersRepository;
import com.example.project.security.config.JwtService;
import com.example.project.security.token.Token;
import com.example.project.security.token.TokenRepository;
import com.example.project.security.token.TokenType;
import com.example.project.security.user.Role;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
  private final UsersRepository repository;
  private final TokenRepository tokenRepository;
  private final PasswordEncoder passwordEncoder;
  private final JwtService jwtService;
  private final AuthenticationManager authenticationManager;
  private final RestTemplate restTemplate; // 주입받아서 사용
  
  
  
  public AuthenticationResponse register(RegisterRequest request) {
    var user = Users.builder()
        .nickname(request.getNickname())
        .gender(request.getGender())
        .email(request.getEmail())
        .password(passwordEncoder.encode(request.getPassword()))
        .build();
    var savedUser = repository.save(user);
    var jwtToken = jwtService.generateToken(user);
    var refreshToken = jwtService.generateRefreshToken(user);
    saveUserToken(savedUser, jwtToken);
    return AuthenticationResponse.builder()
        .accessToken(jwtToken)
        .refreshToken(refreshToken)
        .userId(user.getUserId()) // ★ 여기 추가: DB에서 조회한 user의 ID를 넣음
        .build();
  }

  public AuthenticationResponse authenticate(AuthenticationRequest request) {
    authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(
            request.getEmail(),
            request.getPassword()
        )
    );
    var user = repository.findByEmail(request.getEmail())
        .orElseThrow();
    var jwtToken = jwtService.generateToken(user);
    var refreshToken = jwtService.generateRefreshToken(user);
    revokeAllUserTokens(user);
    saveUserToken(user, jwtToken);
    return AuthenticationResponse.builder()
        .accessToken(jwtToken)
        .refreshToken(refreshToken)
        .userId(user.getUserId()) // ★ 여기 추가: DB에서 조회한 user의 ID를 넣음
        .build();
  }

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

  private void revokeAllUserTokens(Users user) {
    var validUserTokens = tokenRepository.findAllValidTokenByUser(user.getUserId());
    if (validUserTokens.isEmpty())
      return;
    validUserTokens.forEach(token -> {
      token.setExpired(true);
      token.setRevoked(true);
    });
    tokenRepository.saveAll(validUserTokens);
  }

  public void refreshToken(
          HttpServletRequest request,
          HttpServletResponse response
  ) throws IOException {
    final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
    final String refreshToken;
    final String userEmail;
    if (authHeader == null ||!authHeader.startsWith("Bearer ")) {
      return;
    }
    refreshToken = authHeader.substring(7);
    userEmail = jwtService.extractUsername(refreshToken);
    if (userEmail != null) {
      var user = this.repository.findByEmail(userEmail)
              .orElseThrow();
      if (jwtService.isTokenValid(refreshToken, user)) {
        var accessToken = jwtService.generateToken(user);
        revokeAllUserTokens(user);
        saveUserToken(user, accessToken);
        var authResponse = AuthenticationResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
        new ObjectMapper().writeValue(response.getOutputStream(), authResponse);
      }
    }
  }
  
  /**
   * 보편적인 SNS 로그인 처리
   */
  public AuthenticationResponse socialLogin(SocialLoginRequest request) {
      // 1. (선택사항) 필요시 idToken으로 구글/카카오 서버에 유효성 검증 로직 추가 가능
      // 여기서는 클라이언트가 준 정보를 믿고 가입/로그인을 진행하는 보편적 흐름으로 작성합니다.

      // 2. DB에서 이메일로 유저 찾기 없으면 새로 생성(회원가입)
      var user = repository.findByEmail(request.getEmail())
              .orElseGet(() -> {
                  // 신규 유저일 경우 전달받은 모든 상세 정보 저장
                  Users newUser = Users.builder()
                          .email(request.getEmail())
                          .nickname(request.getNickname())
                          // 아래 필드들은 귀하의 Users 엔티티 컬럼명에 맞춰 수정하세요
                          //.age(request.getAgeRange()) 
                          .job(request.getJobCategory())
                          .zipCode(request.getZipCode())
                          .address(request.getBasicAddress() + " " + request.getDetailAddress())
                          .build();
                  return repository.save(newUser);
              });

      // 3. 기존에 구축하신 JWT 발급 로직 재사용
      var jwtToken = jwtService.generateToken(user);
      var refreshToken = jwtService.generateRefreshToken(user);
      
      // 기존 토큰 만료 처리 및 새 토큰 저장 (기존에 작성하신 메서드 호출)
      revokeAllUserTokens(user);
      saveUserToken(user, jwtToken);

      return AuthenticationResponse.builder()
              .accessToken(jwtToken)
              .refreshToken(refreshToken)
              .userId(user.getUserId())
              .build();
  }

  

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
      Map<String, Object> kakaoAccount = (Map<String, Object>) body.get("kakao_account");
      Map<String, Object> profile = (Map<String, Object>) kakaoAccount.get("profile");

      return new SocialUserInfo(
          (String) kakaoAccount.get("email"),
          (String) profile.get("nickname")
      );
  }

  private SocialUserInfo getGoogleUserInfo(String token) {
      RestTemplate restTemplate = new RestTemplate();
      // 구글 토큰 정보 조회 엔드포인트
      String url = "https://www.googleapis.com/oauth2/v3/userinfo?access_token=" + token;
      
      ResponseEntity<Map> response = restTemplate.getForEntity(url, Map.class);
      Map<String, Object> body = response.getBody();

      return new SocialUserInfo(
          (String) body.get("email"),
          (String) body.get("name")
      );
  }

  // 내부 Helper 클래스
  @lombok.Getter
  @lombok.AllArgsConstructor
  private static class SocialUserInfo {
      private String email;
      private String nickname;
  }

  
  
}
