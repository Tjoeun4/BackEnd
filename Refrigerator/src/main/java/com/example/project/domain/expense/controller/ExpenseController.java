package com.example.project.domain.expense.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.project.domain.expense.dto.ExpenseRequest;
import com.example.project.domain.expense.dto.ExpenseResponse;
import com.example.project.domain.expense.dto.MonthlyDailySummaryResponse;
import com.example.project.domain.expense.service.ExpenseService;
import com.example.project.member.domain.Users;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/expenses")
@RequiredArgsConstructor
public class ExpenseController {

    private final ExpenseService expenseService;

    /**
     * 1. 지출 내역 생성
     * @Valid를 통해 amount가 0 이상인지 검증합니다.
     */
    @PostMapping
    public ResponseEntity<Long> createExpense(
            @RequestBody @Valid ExpenseRequest request,
            @AuthenticationPrincipal Users userDetails) {
        
        Long expenseId = expenseService.createExpense(request, userDetails);
        return ResponseEntity.status(HttpStatus.CREATED).body(expenseId);
    }

    /**
     * 2. 내역 목록 조회 (페이징)
     * 기본값: 한 페이지에 15개, 최신 소비일(spentAt) 순으로 정렬
     */
    @GetMapping
    public ResponseEntity<Page<ExpenseResponse>> getExpenses(
            @AuthenticationPrincipal Users userDetails,
            @PageableDefault(size = 15, sort = "spentAt", direction = Sort.Direction.DESC) Pageable pageable) {
        
        Page<ExpenseResponse> responses = expenseService.getExpenses(userDetails.getUserId(), pageable);
        return ResponseEntity.ok(responses);
    }

    /**
     * 3. 상세 조회
     */
    @GetMapping("/{id}")
    public ResponseEntity<ExpenseResponse> getExpenseDetail(
            @PathVariable("id") Long id,
            @AuthenticationPrincipal Users userDetails) {
        
        return ResponseEntity.ok(expenseService.getExpenseDetail(id, userDetails.getUserId()));
    }

    /**
     * 4. 내역 수정
     */
    @PutMapping("/{id}")
    public ResponseEntity<Void> updateExpense(
            @PathVariable("id") Long id,
            @RequestBody @Valid ExpenseRequest request,
            @AuthenticationPrincipal Users userDetails) {
        
        expenseService.updateExpense(id, request, userDetails.getUserId());
        return ResponseEntity.ok().build();
    }

    /**
     * 5. 내역 삭제
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteExpense(
            @PathVariable("id") Long id,
            @AuthenticationPrincipal Users userDetails) {
        
        expenseService.deleteExpense(id, userDetails.getUserId());
        return ResponseEntity.noContent().build();
    }
    
 // 1. 월별 지출 목록 (최신순 페이징)
    @GetMapping("/monthly")
    public ResponseEntity<Page<ExpenseResponse>> getMonthlyExpenses(
            @AuthenticationPrincipal Users userDetails,
            @RequestParam("year") int year,    // ✅ ("year") 추가
            @RequestParam("month") int month,  // ✅ ("month") 추가
            @PageableDefault(size = 15, sort = "spentAt", direction = Sort.Direction.DESC) Pageable pageable) {
        
        return ResponseEntity.ok(expenseService.getMonthlyExpenses(userDetails.getUserId(), year, month, pageable));
    }

    // 2. 월별 일일 요약 (달력 표시용)
    @GetMapping("/monthly/daily-summary")
    public ResponseEntity<MonthlyDailySummaryResponse> getDailySummary(
            @AuthenticationPrincipal Users userDetails,
            @RequestParam("year") int year,    // ✅ ("year") 추가
            @RequestParam("month") int month) { // ✅ ("month") 추가
        
        return ResponseEntity.ok(expenseService.getDailySummary(userDetails.getUserId(), year, month));
    }

    // 3. 특정 날짜 상세 조회
    @GetMapping("/daily")
    public ResponseEntity<List<ExpenseResponse>> getDailyExpenses(
            @AuthenticationPrincipal Users userDetails,
            @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) { // ✅ ("date") 추가
        
        return ResponseEntity.ok(expenseService.getDailyExpenses(userDetails.getUserId(), date));
    }
}