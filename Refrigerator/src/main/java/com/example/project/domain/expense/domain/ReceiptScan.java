package com.example.project.domain.expense.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.example.project.member.domain.Users;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "receipt_scans")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder // 이 어노테이션이 있어야 .builder()를 쓸 수 있습니다.
@AllArgsConstructor
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
    private Long totalAmount;

    @Column(name = "purchased_at")
    private LocalDateTime purchasedAt;

    @OneToMany(mappedBy = "receipt", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ReceiptItem> items = new ArrayList<>();
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private Users user;
}
