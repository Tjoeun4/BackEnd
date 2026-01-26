package com.example.project.domain.fridge.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import com.example.project.domain.fridge.domain.FridgeItem;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class FridgeItemDto {

    private Long fridgeItemId;
    private Long itemId;
    private String itemName;
    private String rawName;
    private BigDecimal quantity;
    private String unit;
    private LocalDate purchaseDate;
    private LocalDate expiryDate;
    private String status;
    /** 유통기한까지 남은 일수 (null 가능) */
    private Integer daysLeft;
    private LocalDateTime createdAt;

    public static FridgeItemDto from(FridgeItem f) {
        Integer daysLeft = null;
        if (f.getExpiryDate() != null) {
            daysLeft = (int) ChronoUnit.DAYS.between(LocalDate.now(), f.getExpiryDate());
        }
        String itemName = (f.getItem() != null) ? f.getItem().getName() : null;
        Long itemId = (f.getItem() != null) ? f.getItem().getItemId() : null;
        return new FridgeItemDto(
            f.getFridgeItemId(),
            itemId,
            itemName,
            f.getRawName(),
            f.getQuantity(),
            f.getUnit(),
            f.getPurchaseDate(),
            f.getExpiryDate(),
            f.getStatus(),
            daysLeft,
            f.getCreatedAt()
        );
    }
}
