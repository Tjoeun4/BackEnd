package com.example.project.domain.expense.service;

// 1. 자바 표준 라이브러리 (날짜, 리스트 관련)
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors; // [추가] Collectors 에러 해결

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
import com.example.project.domain.expense.repository.ReceiptItemRepository;
// 4. 회원(Member) 관련 도메인 및 레포지토리
import com.example.project.member.domain.Users;
import com.example.project.member.repository.UsersRepository;

// 5. Lombok (생성자 주입 자동화)
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReceiptService {
    private final ExpenseRepository expenseRepository;
    private final UsersRepository userRepository;

 // 이름을 ReceiptGeminiClient로 변경
    private final ReceiptGeminiClient receiptGeminiClient;
    private final ReceiptItemRepository receiptItemRepository;
    
    @Transactional
    public Long processReceiptImage(MultipartFile file, Long userId) {
        Users user = userRepository.getReferenceById(userId);

        // 1. Gemini API 호출
        ReceiptAnalysisResponse analysis = receiptGeminiClient.analyzeReceiptImage(file);
        System.out.println("[DEBUG] 서비스 단계 진입 - 분석 결과 수신 완료");

        // 2. 날짜 파싱 안전장치 (T가 없는 경우 대비)
        String purchasedAtStr = analysis.getPurchasedAt().replace(" ", "T");
        LocalDateTime purchasedAt;
        try {
            purchasedAt = LocalDateTime.parse(purchasedAtStr);
        } catch (Exception e) {
            System.out.println("[WARN] 날짜 파싱 실패, 현재 시간으로 대체합니다: " + analysis.getPurchasedAt());
            purchasedAt = LocalDateTime.now();
        }

        // 3. ReceiptScan 생성
        ReceiptScan scan = ReceiptScan.builder()
                .totalAmount(analysis.getTotalAmount())
                .purchasedAt(purchasedAt)
                .user(user)
                .build();

        // 4. ReceiptItem 생성 및 저장 로직 추가
        List<ReceiptItem> items = analysis.getItems().stream()
                .map(itemDto -> ReceiptItem.builder()
                        .itemName(itemDto.getItemName())
                        .unitAmount(itemDto.getUnitAmount())
                        .quantity(itemDto.getQuantity())
                        .amount(itemDto.getAmount())
                        .price(itemDto.getPrice())
                        .category(itemDto.getCategory())
                        .isFridgeTarget(itemDto.getIsFridgeTarget())
                        .receipt(scan) // 연관관계 설정
                        .build())
                .collect(Collectors.toList());
        
        
        receiptItemRepository.saveAll(items);
        // 만약 ReceiptScan에 @OneToMany(cascade = CascadeType.ALL) 처리가 안 되어 있다면 
        // 개별 Repository에서 itemRepository.saveAll(items); 를 호출해야 합니다.

        // 5. 통합 지출(Expense) 생성
        Expense expense = Expense.builder()
                .title(analysis.getStoreName())
                .spentAt(purchasedAt) // spentAt 누락 방지
                .amount(analysis.getTotalAmount())
                .category(Category.RECEIPT)
                .memo(analysis.getSummaryMemo())
                .receiptScan(scan)
                .user(user)
                .build();

        System.out.println("[DEBUG] 최종 DB 저장 직전: " + analysis.getStoreName());
        return expenseRepository.save(expense).getExpenseId();
    }
}
