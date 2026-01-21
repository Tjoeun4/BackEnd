package com.example.project.domain.expense.dto;

import java.util.List;



public record MonthlyDailySummaryResponse(
	    int year,
	    int month,
	    Long monthTotalAmount,
	    List<DailyAmount> dailyAmounts // 예: [ {1일: 5000}, {2일: 0}, {3일: 12000} ... ]
	) {}