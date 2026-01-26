package com.example.project.domain.fridge.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.project.domain.fridge.domain.FoodCategoryType;
import com.example.project.domain.fridge.domain.FridgeItem;
import com.example.project.domain.fridge.domain.ItemAlias;
import com.example.project.domain.fridge.domain.Items;
import com.example.project.domain.fridge.dto.IngredientCreateBulkRequest;
import com.example.project.domain.fridge.dto.IngredientCreateRequest;
import com.example.project.domain.fridge.dto.IngredientCreateResponse;
import com.example.project.domain.fridge.dto.IngredientResolveMultiRequest;
import com.example.project.domain.fridge.dto.IngredientResolveRequest;
import com.example.project.domain.fridge.dto.IngredientResolveResponse;
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
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class IngredientAddService {

    private final ItemAliasRepository itemAliasRepository;
    private final ItemsRepository itemsRepository;
    private final UsersRepository usersRepository;
    private final FridgeItemRepository fridgeItemRepository;
    private final IngredientInferClient ingredientInferClient;

    @Transactional(readOnly = true)
    public IngredientResolveResponse resolve(IngredientResolveRequest req) {
        return resolveOne(req.getInputName());
    }

    // 여러 개 한 번에 resolve 
    @Transactional(readOnly = true)
    public List<IngredientResolveResponse> resolveMulti(IngredientResolveMultiRequest req) {
        List<String> names = req.getInputNames() != null ? req.getInputNames() : List.of();
        List<IngredientResolveResponse> results = new ArrayList<>();
        for (String name : names) {
            if (name == null || name.isBlank()) {
                results.add(IngredientResolveResponse.aiPending("입력값이 비어 있습니다."));
            } else {
                results.add(resolveOne(name));
            }
        }
        return results;
    }

    // resolve 1건 (resolve / resolveMulti 공통 로직)
    private IngredientResolveResponse resolveOne(String inputName) {
        String input = normalize(inputName);

        // 2-1) alias 완전일치
        var aliasOpt = itemAliasRepository.findByRawName(input);
        if (aliasOpt.isPresent()) {
            ItemAlias alias = aliasOpt.get();
            Items item = alias.getItem();
            var c = new IngredientResolveResponse.AliasCandidate(
                alias.getId(),
                alias.getRawName(),
                item.getId(),
                item.getName()
            );
            return IngredientResolveResponse.confirmAlias(c);
        }

        // 2-2) items 후보 조회 (양방향 매칭)
        List<Items> a = itemsRepository.findByNameContaining(input);
        List<Items> b = itemsRepository.findWhereInputContainsItemName(input);
        Map<Long, Items> merged = new LinkedHashMap<>();
        for (Items i : a) merged.put(i.getId(), i);
        for (Items i : b) merged.put(i.getId(), i);
        List<Items> candidates = new ArrayList<>(merged.values());

        if (!candidates.isEmpty()) {
            List<IngredientResolveResponse.ItemCandidate> list = candidates.stream()
                .map(i -> new IngredientResolveResponse.ItemCandidate(
                    i.getId(),
                    i.getName(),
                    i.getExpirationNum()
                ))
                .toList();
            return IngredientResolveResponse.pickItem(list);
        }

        // 2-3) 후보 없으면 AI
        return IngredientResolveResponse.aiPending("매칭되는 식재료가 없어 AI로 표준 식재료 생성이 필요합니다.");
    }

    @Transactional
    public IngredientCreateResponse create(IngredientCreateRequest req) {
        Users user = usersRepository.findById(req.getUserId())
            .orElseThrow(() -> new EntityNotFoundException("User not found: " + req.getUserId()));
        return createOne(user, req);
    }

    // 여러 개 한 번에 추가
    @Transactional
    public List<IngredientCreateResponse> createMulti(IngredientCreateBulkRequest multi) {
        Users user = usersRepository.findById(multi.getUserId())
            .orElseThrow(() -> new EntityNotFoundException("User not found: " + multi.getUserId()));

        List<IngredientCreateRequest> items = multi.getItems() != null ? multi.getItems() : List.of();
        List<IngredientCreateResponse> results = new ArrayList<>();

        for (IngredientCreateRequest req : items) {
            if (req == null) continue;
            req.setUserId(multi.getUserId());
            results.add(createOne(user, req));
        }
        return results;
    }

    // 식재료 1개 추가 
    private IngredientCreateResponse createOne(Users user, IngredientCreateRequest req) {
        String inputName = normalize(req.getInputName());

        Items item;
        String rawNameForFridge;

        // 2-1 alias 확정
        if (req.getItemAliasId() != null) {
            ItemAlias alias = itemAliasRepository.findById(req.getItemAliasId())
                .orElseThrow(() -> new EntityNotFoundException("ItemAlias not found: " + req.getItemAliasId()));
            item = alias.getItem();
            rawNameForFridge = alias.getRawName();
        }
        // 2-2 item 선택 확정 → alias 없으면 USER로 생성
        else if (req.getItemId() != null) {
            item = itemsRepository.findById(req.getItemId())
                .orElseThrow(() -> new EntityNotFoundException("Item not found: " + req.getItemId()));

            ItemAlias alias = itemAliasRepository.findByRawName(inputName)
                .orElseGet(() -> itemAliasRepository.save(
                    ItemAlias.create(item, inputName, "USER")
                ));

            rawNameForFridge = alias.getRawName();
        }
        // 2-3 AI 생성 → items + alias(AI) 생성
        else {
            var ai = ingredientInferClient.inferItemInfo(inputName);

            if (ai.itemName() == null || ai.itemName().isBlank()) {
                throw new IllegalStateException("AI itemName 비어있음: " + ai);
            }
            if (ai.expirationDays() == null || ai.expirationDays() < 1 || ai.expirationDays() > 365) {
                throw new IllegalStateException("AI expirationDays 비정상: " + ai);
            }

            FoodCategoryType t = FoodCategoryType.fromLabel(ai.categoryName());
            Long categoryId = (t != null) ? t.id() : FoodCategoryType.VEGETABLE.id(); // fallback: 채소(3)

            item = itemsRepository.save(
                Items.create(ai.itemName().trim(), categoryId, ai.expirationDays().longValue())
            );

            ItemAlias alias = itemAliasRepository.save(
                ItemAlias.create(item, inputName, "AI")
            );

            rawNameForFridge = alias.getRawName();
        }

        LocalDate purchaseDate = req.getPurchaseDate();
        LocalDate expiryDate = calcExpiry(purchaseDate, item.getExpirationNum());

        FridgeItem saved = fridgeItemRepository.save(
            FridgeItem.create(
                user,
                item,
                rawNameForFridge,
                req.getQuantity(),
                req.getUnit(),
                purchaseDate,
                expiryDate
            )
        );

        return new IngredientCreateResponse(
            saved.getFridgeItemId(),
            item.getId(),
            item.getName(),
            rawNameForFridge,
            purchaseDate,
            expiryDate
        );
    }

    private LocalDate calcExpiry(LocalDate purchaseDate, Long expirationNum) {
        if (purchaseDate == null || expirationNum == null) return null;
        return purchaseDate.plusDays(expirationNum);
    }

    private String normalize(String s) {
        return s == null ? null : s.trim();
    }
}
