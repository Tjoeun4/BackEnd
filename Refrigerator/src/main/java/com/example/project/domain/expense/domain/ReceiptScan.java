package com.example.project.domain.expense.domain;

import java.time.LocalDateTime;

import com.example.project.member.domain.Users;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "receipt_scans")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ReceiptScan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long receiptId;

    @Lob
    @Column(name = "ocr_text_raw")
    private String ocrTextRaw;

    @Lob
    @Column(name = "parsed_json")
    private String parsedJson;

    @Column(name = "total_amount")
    private Integer totalAmount;

    @Column(name = "purchased_at")
    private LocalDateTime purchasedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private Users user;
}
