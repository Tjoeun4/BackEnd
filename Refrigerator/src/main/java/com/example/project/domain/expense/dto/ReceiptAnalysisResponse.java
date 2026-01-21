package com.example.project.domain.expense.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReceiptAnalysisResponse {
    private String storeName;
    private Long totalAmount;
    private String purchasedAt;
    private String summaryMemo;
    private List<ReceiptItemDto> items;
}