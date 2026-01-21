package com.example.project.domain.fridge.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public class FridgeItemResponseDto{

    private Long id;
    private String name;
    private BigDecimal quantity;
    private String unit;
    private LocalDate expiryDate;

    public FridgeItemResponseDto(
            Long id,
            String name,
            BigDecimal quantity,
            String unit,
            LocalDate expiryDate
    ) {
        this.id = id;
        this.name = name;
        this.quantity = quantity;
        this.unit = unit;
        this.expiryDate = expiryDate;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

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

