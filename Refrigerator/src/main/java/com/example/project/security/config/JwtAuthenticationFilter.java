package com.example.project.security.config;

import java.io.IOException;

import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.example.project.security.token.TokenRepository;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

  private final JwtService jwtService;
  private final UserDetailsService userDetailsService;
  private final TokenRepository tokenRepository;

  @Override
  protected void doFilterInternal(
      @NonNull HttpServletRequest request,
      @NonNull HttpServletResponse response,
      @NonNull FilterChain filterChain
  ) throws ServletException, IOException {

    // ✅ [진단 0] 필터 진입 로그 (요청마다 1번)
    final String path = request.getServletPath();
    final String method = request.getMethod();
    final String authHeaderRaw = request.getHeader("Authorization");

    log.info("✅ [JWT FILTER ENTER] {} {} | Authorization={}", method, path, authHeaderRaw);

    // 1. Auth 경로는 바로 통과
    if (path.contains("/api/v1/auth")) {
      log.info("➡️ [JWT FILTER BYPASS] auth endpoint: {} {}", method, path);
      filterChain.doFilter(request, response);
      return;
    }

    final String authHeader = authHeaderRaw;
    final String jwt;
    final String userEmail;

    // 2. 헤더가 없거나 Bearer 형식이 아니면 바로 통과 (비로그인)
    if (authHeader == null || !authHeader.startsWith("Bearer ")) {
      log.info("➡️ [JWT FILTER PASS] no/invalid Authorization header. path={} auth={}", path, authHeader);
      filterChain.doFilter(request, response);
      return;
    }

    // 🔥 여기부터 try-catch (기존 로직 유지)
    try {
        jwt = authHeader.substring(7);

        // ✅ [진단 1] Bearer 토큰 원문(앞부분만) 찍기
        String jwtPreview = jwt;
        if (jwtPreview != null && jwtPreview.length() > 25) jwtPreview = jwtPreview.substring(0, 25) + "...";
        log.info("🔎 [JWT TOKEN] extracted='{}' (len={})", jwtPreview, (jwt == null ? 0 : jwt.length()));

        // 3. 프론트엔드가 실수로 보낸 "null" 문자열 방어
        if (jwt == null || jwt.equals("null") || jwt.equals("undefined") || jwt.trim().isEmpty()) {
            log.warn("⚠️ [JWT TOKEN SKIP] token is null/undefined/blank. token='{}'", jwt);
            filterChain.doFilter(request, response);
            return;
        }

        // ✅ [진단 2] extractUsername 전/후 확인
        log.info("🔎 [JWT PARSE] calling jwtService.extractUsername()");
        userEmail = jwtService.extractUsername(jwt);
        log.info("✅ [JWT PARSE OK] extracted userEmail={}", userEmail);

        if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
          log.info("🔎 [JWT AUTH] loading userDetails for {}", userEmail);

          UserDetails userDetails = this.userDetailsService.loadUserByUsername(userEmail);
          log.info("✅ [JWT AUTH OK] userDetails loaded. username={}", userDetails.getUsername());

          // ✅ [진단 3] tokenRepository 조회가 문제인지 확인
          log.info("🔎 [JWT DB] tokenRepository.findByToken() start");
          var isTokenValid = tokenRepository.findByToken(jwt)
              .map(t -> !t.isExpired() && !t.isRevoked())
              .orElse(false);
          log.info("✅ [JWT DB OK] isTokenValid(from DB)={}", isTokenValid);

          // ✅ [진단 4] jwtService 검증이 문제인지 확인
          log.info("🔎 [JWT SIGNATURE] jwtService.isTokenValid() start");
          boolean signatureValid = jwtService.isTokenValid(jwt, userDetails);
          log.info("✅ [JWT SIGNATURE OK] signatureValid={}", signatureValid);

          if (signatureValid && isTokenValid) {
            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                userDetails,
                null,
                userDetails.getAuthorities()
            );
            authToken.setDetails(
                new WebAuthenticationDetailsSource().buildDetails(request)
            );
            SecurityContextHolder.getContext().setAuthentication(authToken);
            log.info("✅ [JWT SET CONTEXT] Authentication set for {}", userEmail);
          } else {
            log.warn("⚠️ [JWT NOT AUTHORIZED] signatureValid={}, dbValid={}", signatureValid, isTokenValid);
          }
        } else {
          log.info("➡️ [JWT SKIP AUTH] userEmail is null OR already authenticated");
        }

    } catch (Exception e) {
        // ✅ [진단 핵심] 예외 타입 + 메시지 + 스택트레이스까지 전부 남김 (로직 변경 X)
        log.error("❌ [JWT ERROR] {} {} | authHeader={} | exceptionType={} message={}",
                method, path, authHeaderRaw,
                e.getClass().getName(), e.getMessage(), e);

        // 기존처럼 “에러 던지지 않고 넘어감” 유지
        System.out.println("⚠️ JWT 오류 발생 (비회원 처리): " + e.getMessage());
    }

    // 5. 다음 필터로 진행 (필수)
    filterChain.doFilter(request, response);
  }
}
