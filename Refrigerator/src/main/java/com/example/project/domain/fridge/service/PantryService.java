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

    @Transactional
    public void addPantryItem(Long userId, String itemName) {
        if (itemName == null || itemName.isBlank()) return;

        pantryItemRepository.findAnyByUserIdAndName(userId, itemName).ifPresentOrElse(existing -> {
            // 이미 존재하면 delFlag만 복구하고 싶으면 로직 확장 가능
        }, () -> {
            pantryItemRepository.save(new PantryItem(userId, itemName.trim()));
        });
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
