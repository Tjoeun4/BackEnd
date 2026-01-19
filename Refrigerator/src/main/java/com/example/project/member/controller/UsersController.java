package com.example.project.member.controller;

import java.security.Principal;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.project.member.domain.CustomUserDetails;
import com.example.project.member.service.UsersService;
import com.example.project.security.user.ChangePasswordRequest;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UsersController {

    private final UsersService service;


    // =========================
    // 3) 비밀번호 변경
    // =========================
    @PostMapping("/change-password")
    public ResponseEntity<String> changePassword(
            @RequestBody ChangePasswordRequest req,
            Principal principal) {

        service.changePassword(req, principal);
        return ResponseEntity.ok("비밀번호가 변경되었습니다.");
    }

    // =========================
    // 4) 회원 탈퇴
    // =========================
    @PostMapping("/delete")
    public ResponseEntity<String> deleteUser(Principal principal) {
        service.deleteUser(principal);
        return ResponseEntity.ok("회원 탈퇴 처리되었습니다.");
    }

    // =========================
    // 5) 회원 복구
    // =========================
    @PostMapping("/restore")
    public ResponseEntity<String> restoreUser(Principal principal) {
        service.restoreUser(principal);
        return ResponseEntity.ok("회원 상태가 복구되었습니다.");
    }

    /**
     * 닉네임 중복 체크 API
     * GET /api/members/check-nickname?nickname=사용자이름
     */
    @GetMapping("/check-nickname")
    public ResponseEntity<Map<String, Boolean>> checkNickname(@RequestParam("nickname") String nickname) {
        boolean isDuplicated = service.isNicknameDuplicated(nickname);
        // JSON 형태로 반환: {"isDuplicated": true/false}
        return ResponseEntity.ok(Map.of("isDuplicated", isDuplicated));
    }
    /**
     * FCM 토큰 등록 및 업데이트를 위한 컨트롤러 메서드
     * @AuthenticationPrincipal: 인증된 사용자의 정보(세션/JWT)를 자동으로 주입받습니다.
     * @RequestBody: 클라이언트가 보낸 JSON 데이터 {"fcmToken": "..."}를 Map 형태로 읽어옵니다.
     */
    @PostMapping("/fcm-token")
    public ResponseEntity<Void> updateFcmToken(
        @AuthenticationPrincipal CustomUserDetails userDetails, // 1. 현재 로그인한 유저의 정보를 가져옵니다.
        @RequestBody Map<String, String> body // 2. 프론트엔드가 보낸 JSON 본문을 Map 객체로 변환하여 받습니다.
    ) {
        // 3. Map 객체 안에서 "fcmToken"이라는 Key를 가진 값(문자열 토큰)을 추출합니다.
        String fcmToken = body.get("fcmToken");
        
        // 4. 서비스 계층으로 유저의 고유 ID(PK)와 추출한 토큰 값을 넘겨 로직을 처리하게 합니다.
        service.updateFcmToken(userDetails.getUserId(), fcmToken);
        
        // 5. 작업이 성공적으로 끝났음을 알리는 200 OK 상태 코드를 반환합니다.
        return ResponseEntity.ok().build();
    }
    
    // 설문조사 TRUE 로 바꿔주는 함ㅅ
    @PostMapping("/onboarding/complete")
    public ResponseEntity<String> completeOnboarding(Principal principal) {
        service.completeOnboarding(principal);
        return ResponseEntity.ok("온보딩 설문이 완료되었습니다.");
    }
    
}
