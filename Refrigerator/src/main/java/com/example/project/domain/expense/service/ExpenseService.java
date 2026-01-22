package com.example.project.domain.expense.service;

import java.time.LocalDate;
import java.time.LocalDateTime; // LocalDateTime 추가 확인
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.project.domain.expense.domain.Category; // 올바른 Category import
import com.example.project.domain.expense.domain.Expense;
import com.example.project.domain.expense.domain.ReceiptScan;
import com.example.project.domain.expense.dto.DailyAmount;
import com.example.project.domain.expense.dto.ExpenseRequest;
import com.example.project.domain.expense.dto.ExpenseResponse;
import com.example.project.domain.expense.dto.ExpenseStatisticsResponse;
import com.example.project.domain.expense.dto.MonthlyDailySummaryResponse;
import com.example.project.domain.expense.repository.ExpenseRepository;
import com.example.project.member.domain.Users;
import com.example.project.member.repository.UsersRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ExpenseService {

    private final ExpenseRepository expenseRepository;
    private final UsersRepository userRepository;

    @Transactional
    public Long createExpense(ExpenseRequest dto, Users user) {
        Users persistUser = userRepository.getReferenceById(user.getUserId());

        // 일반 수동 입력의 경우
        Expense expense = Expense.builder()
                .spentAt(dto.spentAt())
                .amount(dto.amount())
                .title(dto.title())
                // DTO에서 카테고리가 안 오면 Builder 기본 로직에 의해 ETC로 설정됨
                .category(dto.category()) 
                .memo(dto.memo())
                .user(persistUser)
                .build();

        return expenseRepository.save(expense).getExpenseId();
    }

    /**
     * 영수증 스캔 결과로 지출을 생성할 때 사용하는 별도 메서드 (설계 제안)
     */
    @Transactional
    public Long createExpenseFromReceipt(ReceiptScan scan, String summaryMemo, Users user) {
        Expense expense = Expense.builder()
                .spentAt(scan.getPurchasedAt())
                .amount(scan.getTotalAmount())
                .title("영수증 지출 (" + scan.getPurchasedAt().toLocalDate() + ")")
                .category(Category.RECEIPT) // 영수증 스캔본임을 명시
                .memo(summaryMemo)         // AI가 요약한 텍스트 리스트
                .receiptScan(scan)          // 1:1 관계 연결
                .user(user)
                .build();

        return expenseRepository.save(expense).getExpenseId();
    }
    
    @Transactional
    public void updateExpense(Long id, ExpenseRequest dto, Long currentUserId) {
        // 1. 해당 지출이 존재하고 본인 소유인지 검증 (기존 로직 유지)
        Expense expense = findAndVerifyOwner(id, currentUserId);
        
        // 2. 엔티티의 update 메서드 호출
        // 카테고리가 null로 올 경우 기본값인 ETC를 유지하도록 처리 가능합니다.
        Category targetCategory = (dto.category() != null) ? dto.category() : Category.ETC;

        expense.update(
                dto.spentAt(),
                dto.amount(),
                dto.title(),
                targetCategory,
                dto.memo()
        );
        
        // 영속성 컨텍스트 덕분에 별도의 save 호출 없이 변경 감지(Dirty Checking)로 반영됩니다.
    }

    @Transactional
    public void deleteExpense(Long id, Long currentUserId) {
        Expense expense = findAndVerifyOwner(id, currentUserId);
        expenseRepository.delete(expense);
    }

    public Page<ExpenseResponse> getExpenses(Long userId, Pageable pageable) {
        return expenseRepository.findByUserUserId(userId, pageable)
                .map(ExpenseResponse::from);
    }

    public ExpenseResponse getExpenseDetail(Long id, Long currentUserId) {
        Expense expense = findAndVerifyOwner(id, currentUserId);
        return ExpenseResponse.from(expense);
    }

    private Expense findAndVerifyOwner(Long id, Long userId) {
        Expense expense = expenseRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("해당 지출 내역을 찾을 수 없습니다. ID: " + id));

        if (expense.getUser() == null || !expense.getUser().getUserId().equals(userId)) {
            throw new AccessDeniedException("해당 지출 내역에 대한 권한이 없습니다.");
        }

        return expense;
    }
    
    
 // ExpenseService.java에 추가
    public ExpenseStatisticsResponse getMonthlyStatistics(Long userId, int year, int month) {
        // 1. 해당 월의 시작과 끝 계산
        LocalDateTime start = LocalDateTime.of(year, month, 1, 0, 0);
        LocalDateTime end = start.plusMonths(1).minusNanos(1);

        // 2. DB에서 해당 월의 지출 내역을 모두 가져옴
        List<Expense> monthlyExpenses = expenseRepository.findByUserUserIdAndSpentAtBetween(userId, start, end);

        // 3. 총액 합산
        long totalAmount = monthlyExpenses.stream()
                .mapToLong(Expense::getAmount)
                .sum();

     // Map의 Value 타입을 Integer에서 Long으로 변경합니다.
        Map<Category, Long> categorySum = monthlyExpenses.stream()
                .collect(Collectors.groupingBy(
                        Expense::getCategory,
                        Collectors.summingLong(Expense::getAmount)
                ));

        return new ExpenseStatisticsResponse(totalAmount, categorySum, month);
    }
    
	 
	    
	 // ExpenseService.java 내부에 추가

    // 1. 월별 지출 목록 조회 (페이징)
    public Page<ExpenseResponse> getMonthlyExpenses(Long userId, int year, int month, Pageable pageable) {
        try {
            // month 값 유효성 체크 (1~12 사이여야 함)
            if (month < 1 || month > 12) month = 1;

            LocalDateTime start = LocalDateTime.of(year, month, 1, 0, 0);
            LocalDateTime end = start.plusMonths(1).minusNanos(1);

            return expenseRepository.findByUserUserIdAndSpentAtBetween(userId, start, end, pageable)
                    .map(ExpenseResponse::from);
        } catch (Exception e) {
            // 에러 로그를 남기면 디버깅이 훨씬 쉽습니다.
            log.error("Error fetching monthly expenses for user {}: {}", userId, e.getMessage());
            throw e;
        }
    }

    // 2. 월별 일일 요약 (달력용)
    public MonthlyDailySummaryResponse getDailySummary(Long userId, int year, int month) {
        if (month < 1 || month > 12) month = 1;

        LocalDateTime start = LocalDateTime.of(year, month, 1, 0, 0);
        LocalDateTime end = start.plusMonths(1).minusNanos(1);

        List<DailyAmount> dailyAmounts = expenseRepository.findDailySummaryByMonth(userId, start, end);

        // dailyAmounts가 null인 경우를 대비한 안전한 처리
        Long monthTotalAmount = (dailyAmounts == null) ? 0L : dailyAmounts.stream()
                .mapToLong(DailyAmount::totalAmount)
                .sum();

        return new MonthlyDailySummaryResponse(year, month, monthTotalAmount, dailyAmounts != null ? dailyAmounts : List.of());
    }
	
	 // 3. 특정 날짜 상세 조회 (최신순)
	 public List<ExpenseResponse> getDailyExpenses(Long userId, LocalDate date) {
	     LocalDateTime start = date.atStartOfDay(); // 00:00:00
	     LocalDateTime end = date.atTime(23, 59, 59, 999999999); // 23:59:59.999...
	
	     return expenseRepository.findByUserUserIdAndSpentAtBetweenOrderBySpentAtDesc(userId, start, end)
	             .stream()
	             .map(ExpenseResponse::from) //
	             .toList();
	 }
	    
	    
	}