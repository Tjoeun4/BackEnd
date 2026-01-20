package com.example.project.domain.expense.repository;


import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.project.domain.expense.domain.Category;
import com.example.project.domain.expense.domain.Expense;

public interface ExpenseRepository extends JpaRepository<Expense, Long> {
    // 유저별 지출 내역을 페이징하여 조회
    Page<Expense> findByUserUserId(Long userId, Pageable pageable);

	List<Expense> findAllByUser_UserIdAndSpentAtBetween(Long userId, LocalDateTime start, LocalDateTime end);

    // 2번: 기간 및 카테고리 필터링 (Querydsl 대용)
    Page<Expense> findByUserUserIdAndSpentAtBetweenAndCategory(
        Long userId, LocalDateTime start, LocalDateTime end, Category category, Pageable pageable);

    // 3번: 통계용 데이터 조회 (특정 월의 모든 데이터)
    List<Expense> findByUserUserIdAndSpentAtBetween(Long userId, LocalDateTime start, LocalDateTime end);
}