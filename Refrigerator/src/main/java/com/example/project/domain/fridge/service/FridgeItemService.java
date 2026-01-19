package com.example.project.domain.fridge.service;

import java.time.LocalDate;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.project.domain.fridge.domain.FridgeItem;
import com.example.project.domain.fridge.domain.ItemAlias;
import com.example.project.domain.fridge.domain.Items;
import com.example.project.domain.fridge.dto.FridgeItemDto;
import com.example.project.domain.fridge.repository.FridgeItemRepository;
import com.example.project.domain.fridge.repository.ItemAliasRepository;
import com.example.project.domain.fridge.repository.ItemRepository;
import com.example.project.member.domain.Users;
import com.example.project.member.repository.UsersRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FridgeItemService {

    private final UsersRepository usersRepository;
    private final ItemRepository itemRepository;
    private final ItemAliasRepository aliasRepository;
    private final FridgeItemRepository fridgeItemRepository;
    private final AiItemService aiItemService;

    @Transactional
    public FridgeItemDto.Response create(Long userId, FridgeItemDto.Request req) {
        Users user = usersRepository.findById(userId).orElseThrow();

        String rawInputName = req.getRawInputName();
        LocalDate purchaseDate = (req.getPurchaseDate() != null) ? req.getPurchaseDate() : LocalDate.now();

        Items item;
        String mode;

        boolean confirmed = Boolean.TRUE.equals(req.getConfirmed());
        if (confirmed && req.getMatchedItemId() != null) {
            item = itemRepository.findById(req.getMatchedItemId()).orElseThrow();
            mode = "EXISTING";

            // alias 누적(없으면만)
            if (!aliasRepository.existsByAlias(rawInputName)) {
                aliasRepository.save(ItemAlias.create(item, rawInputName, "USER"));
            }

        } else {
            // 신규: AI 생성
            Items created = aiItemService.createItemFromAi(rawInputName);
            item = itemRepository.save(created);
            mode = "AI_CREATED";

            // 입력 원문을 새 item의 alias로 저장
            if (!aliasRepository.existsByAlias(rawInputName)) {
                aliasRepository.save(ItemAlias.create(item, rawInputName, "AI"));
            }
        }

        LocalDate expiryDate = purchaseDate.plusDays(item.getExpirationNum());

        // 여기서부턴 냉장고 내 아이템 생성
        FridgeItem fi = FridgeItem.create(
                user, item, rawInputName,
                req.getQuantity(), req.getUnit(),
                purchaseDate, expiryDate
        );

        FridgeItem saved = fridgeItemRepository.save(fi);

        return new FridgeItemDto.Response(
                saved.getFridgeItemId(),
                item.getId(),
                item.getName(),
                expiryDate,
                mode
        );
    }
}
