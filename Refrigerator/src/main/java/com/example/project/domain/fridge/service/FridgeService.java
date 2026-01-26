package com.example.project.domain.fridge.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.project.domain.fridge.domain.FridgeItem;
import com.example.project.domain.fridge.dto.FridgeItemDto;
import com.example.project.domain.fridge.repository.FridgeItemRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FridgeService {

    private final FridgeItemRepository fridgeItemRepository;

    // 해당 사용자의 ACTIVE 냉장고 재료 목록 조회 (유통기한 임박 순)
    @Transactional(readOnly = true)
    public List<FridgeItemDto> getActiveItems(Long userId) {
        List<FridgeItem> list = fridgeItemRepository.findActiveItems(userId);
        List<FridgeItemDto> dtos = list.stream()
            .map(FridgeItemDto::from)
            .toList();
        // 유통기한 임박(daysLeft 작은) 순 정렬
        return dtos.stream()
            .sorted((a, b) -> {
                Integer da = a.getDaysLeft();
                Integer db = b.getDaysLeft();
                if (da == null && db == null) return 0;
                if (da == null) return 1;
                if (db == null) return -1;
                return da.compareTo(db);
            })
            .toList();
    }

    //해당 사용자의 ACTIVE 냉장고 재료 목록 조회 (정렬 없음, DB 조회 순)
    @Transactional(readOnly = true)
    public List<FridgeItemDto> getActiveItemsOnly(Long userId) {
        List<FridgeItem> list = fridgeItemRepository.findActiveItems(userId);
        return list.stream()
            .map(FridgeItemDto::from)
            .toList();
    }

    // 냉장고 재료 소프트 삭제 (status=REMOVED). 본인 소유만 가능
    @Transactional
    public void remove(Long userId, Long fridgeItemId) {
        FridgeItem f = fridgeItemRepository.findByFridgeItemIdAndUser_UserId(fridgeItemId, userId)
            .orElseThrow(() -> new EntityNotFoundException("FridgeItem not found or access denied"));
        f.markAsRemoved();
    }

    // status=REMOVED 이고 updatedAt이 10일 이전인 행을 DB에서 완전 삭제 - 매일 새벽 3시 실행
    @Scheduled(cron = "0 0 3 * * ?")
    @Transactional
    public void permanentlyDeleteRemovedAfter10Days() {
        LocalDateTime cutoff = LocalDateTime.now().minusDays(10);
        List<FridgeItem> toDelete = fridgeItemRepository.findByStatusAndUpdatedAtBefore("REMOVED", cutoff);
        fridgeItemRepository.deleteAll(toDelete);
    }
}
