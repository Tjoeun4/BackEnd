package com.example.project.domain.fridge.service;

import java.util.*;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.project.domain.fridge.domain.PantryItem;
import com.example.project.domain.fridge.dto.PantryItemDto;
import com.example.project.domain.fridge.repository.PantryItemRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PantryService {

    private final PantryItemRepository pantryItemRepository;

    private static final List<String> DEFAULT_PANTRY = List.of(
        "마늘", "고추장", "설탕", "소금", "식용유"
    );

    @Transactional
    public void seedDefaultIfEmpty(Long userId) {
        List<PantryItem> current = pantryItemRepository.findActiveByUserId(userId);
        if (!current.isEmpty()) return;

        for (String name : DEFAULT_PANTRY) {
            pantryItemRepository.save(new PantryItem(userId, name));
        }
    }

    @Transactional(readOnly = true)
    public List<PantryItemDto> getActivePantryItemDtos(Long userId) {
        return pantryItemRepository.findActiveByUserId(userId).stream()
            .map(p -> new PantryItemDto(p.getPantryItemId(), p.getItemName()))
            .toList();
    }

    @Transactional(readOnly = true)
    public Set<String> getActivePantryNames(Long userId) {
        return pantryItemRepository.findActiveByUserId(userId).stream()
            .map(PantryItem::getItemName)
            .map(this::normalize)
            .filter(s -> !s.isBlank())
            .collect(Collectors.toCollection(LinkedHashSet::new));
    }

    /** 활성 항목이 이미 존재하는지 확인 */
    @Transactional(readOnly = true)
    public boolean isActiveItemExists(Long userId, String itemName) {
        if (itemName == null || itemName.isBlank()) return false;
        return pantryItemRepository.findActiveByUserIdAndName(userId, itemName.trim()).isPresent();
    }

    @Transactional
    public void addPantryItem(Long userId, String itemName) {
        if (itemName == null || itemName.isBlank()) return;

        String trimmedName = itemName.trim();
        
        // 활성 항목(delFlag='N')이 이미 있는지 확인 
        Optional<PantryItem> activeItem = pantryItemRepository.findActiveByUserIdAndName(userId, trimmedName);
        if (activeItem.isPresent()) {
            // 이미 활성 항목이 있으면 중복 추가 방지
            return;
        }

        // 2. 삭제된 항목(delFlag='Y')이 있는지 확인
        Optional<PantryItem> deletedItem = pantryItemRepository.findAnyByUserIdAndName(userId, trimmedName);
        if (deletedItem.isPresent()) {
            PantryItem item = deletedItem.get();
            // 삭제된 항목이 있으면 복구
            item.restore();
            return;
        }

        // 3. 완전히 새로운 항목이면 생성
        pantryItemRepository.save(new PantryItem(userId, trimmedName));
    }

    @Transactional
    public void removePantryItem(Long pantryItemId) {
        PantryItem item = pantryItemRepository.findById(pantryItemId)
            .orElseThrow(() -> new IllegalArgumentException("PANTRY_ITEM_NOT_FOUND"));
        item.delete();
    }

    private String normalize(String s) {
        return s == null ? "" : s.trim().toLowerCase().replaceAll("\\s+", " ");
    }
}
