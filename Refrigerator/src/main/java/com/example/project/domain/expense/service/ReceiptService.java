package com.example.project.domain.expense.service;

// 1. 자바 표준 라이브러리 (날짜, 리스트 관련)
import java.time.LocalDateTime;
import java.util.List;

// 2. 스프링 프레임워크 (서비스 및 트랜잭션 관련)
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

// 3. 프로젝트 내 도메인 및 카테고리 (경로는 실제 프로젝트 구조에 맞게 조정 필요)
import com.example.project.domain.expense.domain.Category;
import com.example.project.domain.expense.domain.Expense;
import com.example.project.domain.expense.domain.ReceiptItem;
import com.example.project.domain.expense.domain.ReceiptScan;
import com.example.project.domain.expense.dto.ReceiptAnalysisResponse;
import com.example.project.domain.expense.repository.ExpenseRepository;
// 4. 회원(Member) 관련 도메인 및 레포지토리
import com.example.project.member.domain.Users;
import com.example.project.member.repository.UsersRepository;

// 5. Lombok (생성자 주입 자동화)
import lombok.RequiredArgsConstructor;
import java.util.stream.Collectors; // [추가] Collectors 에러 해결

@Service
@RequiredArgsConstructor
public class ReceiptService {
    private final ExpenseRepository expenseRepository;
    private final UsersRepository userRepository;

 // 이름을 ReceiptGeminiClient로 변경
    private final ReceiptGeminiClient receiptGeminiClient;
    
    
    @Transactional
    public Long processReceiptImage(MultipartFile file, Long userId) {
        Users user = userRepository.getReferenceById(userId);

        // 1. Gemini API 호출
        ReceiptAnalysisResponse analysis = receiptGeminiClient.analyzeReceiptImage(file);

        // 2. ReceiptScan(마스터) 생성 (Getter 사용으로 수정)
        ReceiptScan scan = ReceiptScan.builder()
                .totalAmount(analysis.getTotalAmount()) // .totalAmount() -> .getTotalAmount()
                .purchasedAt(LocalDateTime.parse(analysis.getPurchasedAt())) // .purchasedAt() -> .getPurchasedAt()
                .user(user)
                .build();

        List<ReceiptItem> items = analysis.getItems().stream()
                .<ReceiptItem>map(itemDto -> ReceiptItem.builder()
                        .itemName(itemDto.getItemName())
                        .unitAmount(itemDto.getUnitAmount())
                        .quantity(itemDto.getQuantity())
                        .amount(itemDto.getAmount()) // [추가] AI가 분석한 "500g" 등의 용량 정보
                        .price(itemDto.getPrice())
                        .category(itemDto.getCategory())
                        .isFridgeTarget(itemDto.getIsFridgeTarget())
                        .receipt(scan)
                        .build())
                .collect(Collectors.toList());
        
        
        
        // 4. 통합 지출(Expense) 생성 및 연결 (Getter 사용으로 수정)
        Expense expense = Expense.builder()
                .title(analysis.getStoreName()) // .storeName() -> .getStoreName()
                .amount(analysis.getTotalAmount()) // .totalAmount() -> .getTotalAmount()
                .category(Category.RECEIPT)
                .memo(analysis.getSummaryMemo()) // .summaryMemo() -> .getSummaryMemo()
                .receiptScan(scan)
                .user(user)
                .build();

        return expenseRepository.save(expense).getExpenseId();
    }
}
