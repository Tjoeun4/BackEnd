package com.example.project.domain.expense.repository;

import com.example.project.domain.expense.domain.ReceiptScan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReceiptScanRepository extends JpaRepository<ReceiptScan, Long> {
    
    // 특정 사용자의 영수증 스캔 내역을 최신순으로 조회하는 메서드 (필요 시 사용)
    List<ReceiptScan> findByUserUserIdOrderByPurchasedAtDesc(Long userId);
}