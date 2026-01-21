package com.example.project.domain.fridge.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public class FridgeItemCreateRequestDto {

    private Long itemId;          
    private String name;
    private BigDecimal quantity;
    private String unit;
    private LocalDate purchaseDate;
    private LocalDate expiryDate;

    public Long getItemId() { return itemId; }
    public String getName() { return name; }
    public BigDecimal getQuantity() { return quantity; }
    public String getUnit() { return unit; }
    public LocalDate getPurchaseDate() { return purchaseDate; }
    public LocalDate getExpiryDate() { return expiryDate; }
}
