package com.example.project.domain.fridge.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import com.example.project.domain.fridge.domain.PantryItem;

public interface PantryItemRepository extends JpaRepository<PantryItem, Long> {

    @Query("""
        select p
        from PantryItem p
        where p.userId = :userId
          and p.delFlag = 'N'
        order by p.pantryItemId asc
    """)
    List<PantryItem> findActiveByUserId(@Param("userId") Long userId);

    @Query("""
        select p
        from PantryItem p
        where p.userId = :userId
          and lower(p.itemName) = lower(:itemName)
    """)
    Optional<PantryItem> findAnyByUserIdAndName(@Param("userId") Long userId, @Param("itemName") String itemName);
}
