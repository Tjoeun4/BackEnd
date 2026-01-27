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

    /** 활성 항목(delFlag='N') 중에서 이름으로 찾기 */
    @Query("""
        select p
        from PantryItem p
        where p.userId = :userId
          and trim(p.itemName) = trim(:itemName)
          and p.delFlag = 'N'
    """)
    Optional<PantryItem> findActiveByUserIdAndName(@Param("userId") Long userId, @Param("itemName") String itemName);

    /** 삭제된 항목 포함 모든 항목 중에서 이름으로 찾기 */
    @Query("""
        select p
        from PantryItem p
        where p.userId = :userId
          and trim(p.itemName) = trim(:itemName)
    """)
    Optional<PantryItem> findAnyByUserIdAndName(@Param("userId") Long userId, @Param("itemName") String itemName);
}
