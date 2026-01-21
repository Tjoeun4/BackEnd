package com.example.project.domain.expense.controller;

import com.example.project.domain.expense.service.ReceiptService;
import com.example.project.member.domain.Users;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/receipt")
@RequiredArgsConstructor
public class ReceiptController {

    private final ReceiptService receiptService;

    /**
     * 영수증 사진을 업로드하여 지출 내역을 자동 생성합니다.
     */
    @PostMapping("/upload")
    public ResponseEntity<Long> uploadReceipt(
            @RequestParam("file") MultipartFile file,
            @AuthenticationPrincipal Users user // 현재 로그인한 유저 정보
    ) {
        Long expenseId = receiptService.processReceiptImage(file, user.getUserId());
        return ResponseEntity.ok(expenseId);
    }
}