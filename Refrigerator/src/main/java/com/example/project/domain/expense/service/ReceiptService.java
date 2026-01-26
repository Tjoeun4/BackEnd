package com.example.project.domain.expense.service;

import java.math.BigDecimal;
import java.time.LocalDate;
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
import com.example.project.domain.expense.dto.ReceiptItemDto;
import com.example.project.domain.expense.repository.ExpenseRepository;
import com.example.project.domain.expense.repository.ReceiptItemRepository;
import com.example.project.domain.expense.repository.ReceiptScanRepository;
import com.example.project.domain.fridge.domain.FridgeItem;
import com.example.project.domain.fridge.domain.Items;
import com.example.project.domain.fridge.domain.PantryItem;
import com.example.project.domain.fridge.repository.FridgeItemRepository;
import com.example.project.domain.fridge.repository.ItemsRepository;
import com.example.project.domain.fridge.repository.PantryItemRepository;
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
    private final FridgeItemRepository fridgeItemRepository;
    private final PantryItemRepository pantryItemRepository;
 // 이름을 ReceiptGeminiClient로 변경
    private final ReceiptGeminiClient receiptGeminiClient;
    private final ReceiptItemRepository receiptItemRepository;
    private final ItemsRepository itemsRepository; // 상품 마스터 테이블
    private final ReceiptScanRepository receiptScanRepository; // [추가] 필수!
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

        ReceiptScan savedScan = receiptScanRepository.save(scan);
        
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
        
     // 3. 각 항목별 저장 로직 수행
        for (ReceiptItemDto itemDto : analysis.getItems()) {
            saveItemByClassification(user, itemDto, analysis);
        }
        
        
        System.out.println("[DEBUG] 최종 DB 저장 직전: " + analysis.getStoreName());
        return expenseRepository.save(expense).getExpenseId();
    }
    
    private void saveItemByClassification(Users user, ReceiptItemDto itemDto, ReceiptAnalysisResponse analysis) {
        String category = itemDto.getCategory().name();

        // [Case 1] 냉장고 저장 대상 (식재료 + isFridgeTarget=true)
        if ("INGREDIENT".equals(category)) {
            saveToFridge(user, itemDto);
        } 
        // [Case 2] 팬트리/조미료 저장 대상
        // 프롬프트상 'PANTRY' 혹은 'CONDIMENT'로 올 수 있음
        else if ("PANTRY".equals(category)) {
            saveToPantry(user, itemDto);
        }
        // [Case 3] 그 외 (즉석식품, 생활용품 등)
        // 필요하다면 별도 로직 추가 (예: 단순히 가계부 내역에만 남김)
    }

    private void saveToFridge(Users user, ReceiptItemDto itemDto) {
        // 1. Items 마스터 데이터 찾기 (없으면 생성하거나 기본값 매핑 로직 필요)
        Items itemEntity = findOrCreateItem(itemDto.getItemName(), itemDto.getSubCategory());
        // 2. 수량 변환 (int -> BigDecimal)
        BigDecimal quantity = (itemDto.getQuantity() != null) 
                ? BigDecimal.valueOf(itemDto.getQuantity()) 
                : BigDecimal.ZERO;
        
        // 3. FridgeItem 생성
        FridgeItem fridgeItem = FridgeItem.make(
                user,
                (itemDto.getItemName() != null) ? itemDto.getItemName() : "알 수 없는 상품",                
                		
        		quantity,
        		// 단위: amount가 없으면 "단위 미상"
                (itemDto.getAmount() != null) ? itemDto.getAmount() : "0",   // unit (예: 500g)
                LocalDate.now(), // 구매일 (혹은 오늘)
                (itemDto.getSellByDate() != null) ? itemDto.getSellByDate() : LocalDate.now().plusDays(7)
        );
        
        // 소비기한(consumeDate)은 엔티티 필드엔 있지만 create 메서드 파라미터엔 없다면 setter로 주입하거나 create 메서드 수정 필요
        // 여기서는 별도로 설정한다고 가정 (Entity에 Setter가 없으므로 create 메서드 수정 추천)
        // fridgeItem.setConsumeDate(itemDto.getUseByDate()); 
        
        fridgeItemRepository.save(fridgeItem);
    }

    private void saveToPantry(Users user, ReceiptItemDto itemDto) {
        // PantryItem 생성자 사용
        PantryItem pantryItem = new PantryItem(user.getUserId(), itemDto.getItemName());
        pantryItemRepository.save(pantryItem);
    }

    // 헬퍼 메서드: 이름으로 Items 엔티티 찾기 (프로젝트 구조에 맞춰 구현 필요)
    private Items findOrCreateItem(String itemName, String subCategory) {
    	return itemsRepository.findByName(itemName).orElse(null);
    }

    // 헬퍼 메서드: LocalDateTime -> LocalDate 변환 (구매일용)
    private java.time.LocalDate analysisToDate(java.time.LocalDateTime dateTime) {
        return dateTime != null ? dateTime.toLocalDate() : java.time.LocalDate.now();
    }
    
}
