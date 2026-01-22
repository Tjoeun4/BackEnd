package com.example.project.domain.fridge.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.project.domain.fridge.domain.FridgeItem;
import com.example.project.domain.fridge.domain.Items;

public interface FridgeItemRepository extends JpaRepository<FridgeItem, Long> {

    @Query("""
        select f
        from FridgeItem f
        where f.user.userId = :userId
          and f.status = 'ACTIVE'
    """)
    List<FridgeItem> findActiveItems(@Param("userId") Long userId);
    
    
    @Query("""
            select f 
            from FridgeItem f 
            join f.item i 
            where f.user.userId = :userId 
              and f.status = 'ACTIVE' 
              and i.categoryId = :categoryId
        """)
        List<FridgeItem> findActiveItemsByCategory(@Param("userId") Long userId, @Param("categoryId") Long categoryId);


    @Query("SELECT f.item FROM FridgeItem f WHERE f.user.userId = :userId AND f.item.categoryId = :categoryId AND f.status = 'ACTIVE'")
    List<Items> findItemsByUserIdAndCategory(@Param("userId") Long userId, @Param("categoryId") Long categoryId);


}
