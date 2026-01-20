package com.example.project.domain.expense.dto;

import java.time.LocalDateTime;

import com.example.project.domain.expense.domain.Category;

import jakarta.validation.constraints.Min;

public record ExpenseRequest(
	    LocalDateTime spentAt,
	    @Min(value = 0, message = "금액은 0원 이상이어야 합니다.")
	    Integer amount,
	    String title,
	    Category category,
	    String memo
	) {}
