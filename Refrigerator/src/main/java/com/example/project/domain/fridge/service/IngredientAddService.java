package com.example.project.domain.fridge.service;

import com.example.project.domain.fridge.domain.FridgeItem;
import com.example.project.domain.fridge.domain.ItemAlias;
import com.example.project.domain.fridge.domain.Items;
import com.example.project.domain.fridge.dto.*;
import com.example.project.domain.fridge.repository.FridgeItemRepository;
import com.example.project.domain.fridge.repository.ItemAliasRepository;
import com.example.project.domain.fridge.repository.ItemsRepository;
import com.example.project.member.domain.Users;
import com.example.project.member.repository.UsersRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class IngredientAddService {

    private final ItemAliasRepository itemAliasRepository;
    private final ItemsRepository itemsRepository;
    private final UsersRepository usersRepository;
    private final FridgeItemRepository fridgeItemRepository;

    // 이름만 입력 → alias 매칭 / items 후보 / AI
    @Transactional(readOnly = true)
    public IngredientResolveResponse resolve(IngredientResolveRequest req) {
        String input = normalize(req.getInputName());

        // aliases의 raw_name과 정확히 일치
        var aliasOpt = itemAliasRepository.findByRawName(input);
        if (aliasOpt.isPresent()) {
            ItemAlias alias = aliasOpt.get();
            Items item = alias.getItem();
            var c = new IngredientResolveResponse.AliasCandidate(
                alias.getItemAliasId(),
                alias.getRawName(),
                item.getItemId(),
                item.getName()
            );
            return IngredientResolveResponse.confirmAlias(c);
        }

        // items like %name%이 존재할시
        List<Items> candidates = itemsRepository.findByNameContaining(input);
        if (!candidates.isEmpty()) {
            List<IngredientResolveResponse.ItemCandidate> list = candidates.stream()
                .map(i -> new IngredientResolveResponse.ItemCandidate(i.getItemId(), i.getName(), i.getExpirationNum()))
                .toList();
            return IngredientResolveResponse.pickItem(list);
        }

        // 아무것도 없으면 AI (지금은 틀만)
        return IngredientResolveResponse.aiPending("매칭되는 식재료가 없어 AI 추천/생성을 진행해야 합니다. (현재는 미구현)");
    }

    // 사용자 확인 + 상세 입력 → (alias 생성 필요 시 생성) → fridge_item 생성
    @Transactional
    public IngredientCreateResponse create(IngredientCreateRequest req) {
        Users user = usersRepository.findById(req.getUserId())
            .orElseThrow(() -> new EntityNotFoundException("User not found: " + req.getUserId()));

        String inputName = normalize(req.getInputName());

        Items item;
        String rawNameForFridge;

        // alias 기반 확정
        if (req.getItemAliasId() != null) {
            ItemAlias alias = itemAliasRepository.findById(req.getItemAliasId())
                .orElseThrow(() -> new EntityNotFoundException("ItemAlias not found: " + req.getItemAliasId()));

            item = alias.getItem();
            rawNameForFridge = alias.getRawName();
        }
        // item 선택 기반 확정 → alias 없으면 USER source로 추가
        else if (req.getItemId() != null) {
            item = itemsRepository.findById(req.getItemId())
                .orElseThrow(() -> new EntityNotFoundException("Item not found: " + req.getItemId()));

            // alias가 이미 있으면 재사용, 없으면 생성
            ItemAlias alias = itemAliasRepository.findByRawName(inputName)
                .orElseGet(() -> itemAliasRepository.save(ItemAlias.userProvided(item, inputName)));

            rawNameForFridge = alias.getRawName();
        } else {
            throw new IllegalArgumentException("itemAliasId 또는 itemId 중 하나는 반드시 필요합니다.");
        }

        // expiryDate = purchaseDate + expirationNum
        LocalDate purchaseDate = req.getPurchaseDate();
        LocalDate expiryDate = calcExpiry(purchaseDate, item.getExpirationNum());

        FridgeItem fridgeItem = FridgeItem.create(
            user,
            item,
            rawNameForFridge,
            req.getQuantity(),
            req.getUnit(),
            purchaseDate,
            expiryDate
        );

        FridgeItem saved = fridgeItemRepository.save(fridgeItem);

        return new IngredientCreateResponse(
            saved.getFridgeItemId(),
            item.getItemId(),
            item.getName(),
            rawNameForFridge,
            purchaseDate,
            expiryDate
        );
    }

    private LocalDate calcExpiry(LocalDate purchaseDate, Long expirationNum) {
        if (purchaseDate == null) return null;
        if (expirationNum == null) return null;
        return purchaseDate.plusDays(expirationNum);
    }

    private String normalize(String s) {
        if (s == null) return null;
        return s.trim();
    }
}
