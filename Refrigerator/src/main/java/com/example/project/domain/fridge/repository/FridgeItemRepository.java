package com.example.project.domain.fridge.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.project.domain.fridge.domain.FridgeItem;

public interface FridgeItemRepository extends JpaRepository<FridgeItem, Long> {

    @Query("""
        select f
        from FridgeItem f
        where f.user.userId = :userId
          and f.status = 'ACTIVE'
    """)
    List<FridgeItem> findActiveItems(@Param("userId") Long userId);
}
