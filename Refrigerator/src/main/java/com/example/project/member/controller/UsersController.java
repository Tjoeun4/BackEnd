package com.example.project.member.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.project.member.domain.Users;
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

}
