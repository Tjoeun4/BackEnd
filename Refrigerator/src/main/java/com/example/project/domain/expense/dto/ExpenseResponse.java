package com.example.project.domain.expense.dto;

import java.time.LocalDateTime;

import com.example.project.domain.expense.domain.Category;
import com.example.project.domain.expense.domain.Expense;

public record ExpenseResponse(
	    Long expenseId,
	    LocalDateTime spentAt,
	    Long amount,
	    String title,
	    Category category,
	    String memo
	) {
	    public static ExpenseResponse from(Expense expense) {
	        return new ExpenseResponse(
	            expense.getExpenseId(),
	            expense.getSpentAt(),
	            expense.getAmount(),
	            expense.getTitle(),
	            expense.getCategory(),
	            expense.getMemo()
	        );
	    }
	}
