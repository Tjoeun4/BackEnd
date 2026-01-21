package com.example.project.domain.expense.dto;

import java.time.LocalDate;

public record DailyAmount(
	    LocalDate date,
	    Long totalAmount
	) {}
