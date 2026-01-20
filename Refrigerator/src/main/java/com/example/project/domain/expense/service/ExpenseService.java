package com.example.project.domain.expense.service;

import java.time.LocalDateTime; // LocalDateTime 추가 확인
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.project.domain.expense.domain.Category; // 올바른 Category import
import com.example.project.domain.expense.domain.Expense;
import com.example.project.domain.expense.dto.ExpenseRequest;
import com.example.project.domain.expense.dto.ExpenseResponse;
import com.example.project.domain.expense.dto.ExpenseStatisticsResponse;
import com.example.project.domain.expense.repository.ExpenseRepository;
import com.example.project.member.domain.Users;
import com.example.project.member.repository.UsersRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ExpenseService {

    private final ExpenseRepository expenseRepository;
    private final UsersRepository userRepository;

    @Transactional
    public Long createExpense(ExpenseRequest dto, Users user) {
        // 유저 영속성 확보
        Users persistUser = userRepository.getReferenceById(user.getUserId());

        Expense expense = Expense.builder()
                .spentAt(dto.spentAt())
                .amount(dto.amount())
                .title(dto.title())
                .category(dto.category()) // 여기서 이제 에러가 발생하지 않습니다
                .memo(dto.memo())
                .user(persistUser)
                .build();

        return expenseRepository.save(expense).getExpenseId();
    }

    @Transactional
    public void updateExpense(Long id, ExpenseRequest dto, Long currentUserId) {
        Expense expense = findAndVerifyOwner(id, currentUserId);
        
        // 올바른 Category 타입이 전달됩니다
        expense.update(
                dto.spentAt(),
                dto.amount(),
                dto.title(),
                dto.category(),
                dto.memo()
        );
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

        // 4. 카테고리별 합계 계산 (자바 스트림 활용)
        Map<Category, Integer> categorySum = monthlyExpenses.stream()
                .collect(Collectors.groupingBy(
                        Expense::getCategory,
                        Collectors.summingInt(Expense::getAmount)
                ));

        return new ExpenseStatisticsResponse(totalAmount, categorySum, month);
    }
}