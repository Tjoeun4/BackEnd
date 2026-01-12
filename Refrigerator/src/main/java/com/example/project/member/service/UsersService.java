package com.example.project.member.service;

import java.security.Principal;
import java.time.LocalDate;
import java.util.List;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.project.member.domain.Users;
import com.example.project.member.repository.UsersRepository;
import com.example.project.security.user.ChangePasswordRequest;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UsersService {

    private final PasswordEncoder passwordEncoder;
    private final UsersRepository repository;



    // ================================
    // ❗ 3) 회원 탈퇴 (soft delete)
    // ================================
    public void deleteUser(Principal principal) {
        Users user = repository.findByEmail(principal.getName())
                .orElseThrow(() -> new RuntimeException("유저 정보를 찾을 수 없습니다."));

        user.setDelflag("Y");
        user.setDeletedate(LocalDate.now());
        repository.save(user);
    }

    // ================================
    // ❗ 4) 회원 복구
    // ================================
    public void restoreUser(Principal principal) {
        Users user = repository.findByEmail(principal.getName())
                .orElseThrow(() -> new RuntimeException("유저 정보를 찾을 수 없습니다."));

        user.setDelflag("N");
        user.setDeletedate(null);
        repository.save(user);
    }

    // ================================
    // ❗❗❗ 수정 금지 — 원본 그대로 유지 ❗❗❗
    // ================================
    // 비번 변경
    
    public void changePassword(ChangePasswordRequest request, Principal connectedUser) {

        var user = (Users) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();

        // check if the current password is correct
        if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
            throw new IllegalStateException("Wrong password");
        }
        // check if the two new passwords are the same
        if (!request.getNewPassword().equals(request.getConfirmationPassword())) {
            throw new IllegalStateException("Password are not the same");
        }

        // update the password
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));

        // save the new password
        repository.save(user);
    }
}
