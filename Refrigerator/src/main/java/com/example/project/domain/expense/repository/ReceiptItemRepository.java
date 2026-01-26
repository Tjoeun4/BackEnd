package com.example.project.domain.expense.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.project.domain.expense.domain.ReceiptItem;

@Repository
public interface ReceiptItemRepository extends JpaRepository<ReceiptItem, Long> {
    
    // 특정 영수증 스캔본에 속한 모든 품목을 조회하고 싶을 때 사용
    List<ReceiptItem> findByReceiptReceiptId(Long receiptId);
}