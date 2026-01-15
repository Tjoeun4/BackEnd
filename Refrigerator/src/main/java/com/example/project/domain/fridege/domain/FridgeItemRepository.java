package com.example.project.domain.fridege.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FridgeItemRepository extends JpaRepository<FridgeItem, Long> {

    @Query("""
        select f
        from FridgeItem f
        where f.user.id = :userId
          and f.status = 'ACTIVE'
        order by
          case when f.expiryDate is null then 1 else 0 end,
          f.expiryDate asc
    """)
    List<FridgeItem> findActiveByUserOrderByExpiry(@Param("userId") Long userId);
}
