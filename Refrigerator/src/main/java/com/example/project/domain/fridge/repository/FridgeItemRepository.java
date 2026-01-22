package com.example.project.domain.fridge.repository;

import com.example.project.domain.fridge.domain.FridgeItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FridgeItemRepository extends JpaRepository<FridgeItem, Long> {
}
