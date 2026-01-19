package com.example.project.domain.fridge.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.project.domain.fridge.domain.Items;
import com.example.project.domain.fridge.dto.ItemMatchDto;
import com.example.project.domain.fridge.repository.ItemAliasRepository;
import com.example.project.domain.fridge.repository.ItemRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ItemService {

    private final ItemAliasRepository aliasRepo;
    private final ItemRepository itemRepo;

    @Transactional(readOnly = true)
    public ItemMatchDto.Response match(String rawInputName) {
        // 1) alias 정확일치
        var aliasOpt = aliasRepo.findByAlias(rawInputName);
        if (aliasOpt.isPresent()) {
            Items item = aliasOpt.get().getItem();
            return new ItemMatchDto.Response(true, item.getId(), item.getName(), item.getCategoryId(),
                    item.getExpirationNum(), "ALIAS");
        }

        // 2) name 포함 매칭
        var itemOpt = itemRepo.findBestMatchByNameContained(rawInputName);
        if (itemOpt.isPresent()) {
            Items item = itemOpt.get();
            return new ItemMatchDto.Response(true, item.getId(), item.getName(), item.getCategoryId(),
                    item.getExpirationNum(), "NAME");
        }

        return new ItemMatchDto.Response(false, null, null, null, null, "NONE");
    }
}
