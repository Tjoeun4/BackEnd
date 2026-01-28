package com.example.project.domain.expense.repository;


import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.project.domain.expense.domain.Category;
import com.example.project.domain.expense.domain.Expense;
import com.example.project.domain.expense.dto.DailyAmount;

public interface ExpenseRepository extends JpaRepository<Expense, Long> {
    // 유저별 지출 내역을 페이징하여 조회
    Page<Expense> findByUserUserId(Long userId, Pageable pageable);

	List<Expense> findAllByUser_UserIdAndSpentAtBetween(Long userId, LocalDateTime start, LocalDateTime end);

    // 2번: 기간 및 카테고리 필터링 (Querydsl 대용)
    Page<Expense> findByUserUserIdAndSpentAtBetweenAndCategory(
        Long userId, LocalDateTime start, LocalDateTime end, Category category, Pageable pageable);

    // 3번: 통계용 데이터 조회 (특정 월의 모든 데이터)
    List<Expense> findByUserUserIdAndSpentAtBetween(Long userId, LocalDateTime start, LocalDateTime end);
    
    
    // 예시: 특정 기간 내 유저의 지출 조회
    Page<Expense> findByUserUserIdAndSpentAtBetween(Long userId, LocalDateTime start, LocalDateTime end, Pageable pageable);

    @Query("SELECT new com.example.project.domain.expense.dto.DailyAmount(CAST(e.spentAt AS LocalDate), SUM(e.amount)) " +
            "FROM Expense e WHERE e.user.userId = :userId AND e.spentAt BETWEEN :start AND :end " +
            "GROUP BY CAST(e.spentAt AS LocalDate) " +
            "ORDER BY CAST(e.spentAt AS LocalDate) ASC")
    List<DailyAmount> findDailySummaryByMonth(
        @Param("userId") Long userId,     // 👈 @Param 추가
        @Param("start") LocalDateTime start, // 👈 @Param 추가
        @Param("end") LocalDateTime end      // 👈 @Param 추가
    );
 // 3. 특정 날짜 상세 조회 (리스트)
    List<Expense> findByUserUserIdAndSpentAtBetweenOrderBySpentAtDesc(Long userId, LocalDateTime start, LocalDateTime end);
    
    @Query("SELECT e FROM Expense e WHERE e.user.userId = :userId " +
    	       "AND FUNCTION('TO_CHAR', e.spentAt, 'YYYY') = :year " +
    	       "AND FUNCTION('TO_CHAR', e.spentAt, 'MM') = :month")
    	List<Expense> findMonthlyExpenses(
    	    @Param("userId") Long userId, 
    	    @Param("year") String year, 
    	    @Param("month") String month
    	);
}