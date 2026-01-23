package com.example.project.domain.fridge.repository;

import com.example.project.domain.fridge.domain.Items;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ItemsRepository extends JpaRepository<Items, Long> {

    List<Items> findByNameContaining(String name);

    @Query("select i from Items i where :input like concat('%', i.name, '%')")
    List<Items> findWhereInputContainsItemName(@Param("input") String input);
}
