package com.example.project.domain.expense.dto;

import java.util.Map;

import com.example.project.domain.expense.domain.Category;

public record ExpenseStatisticsResponse(
		Long totalAmount,
	    Map<Category, Long> categorySum,
	    int month
	) {}
