package com.example.project.domain.fridge.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.project.domain.fridge.domain.Items;

public interface ItemsRepository extends JpaRepository<Items, Long> {

    List<Items> findByNameContaining(String name);

    @Query("select i from Items i where :input like concat('%', i.name, '%')")
    List<Items> findWhereInputContainsItemName(@Param("input") String input);

	Optional<Items> findByName(String itemName);
}
