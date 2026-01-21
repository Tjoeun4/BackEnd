package com.example.project.domain.fridge.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public class FridgeItemUpdateRequestDto {

    private BigDecimal quantity;
    private String unit;
    private LocalDate expiryDate;

    public BigDecimal getQuantity() {
        return quantity;
    }

    public String getUnit() {
        return unit;
    }

    public LocalDate getExpiryDate() {
        return expiryDate;
    }
}
