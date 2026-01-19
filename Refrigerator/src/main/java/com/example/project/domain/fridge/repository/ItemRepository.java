package com.example.project.domain.fridge.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.project.domain.fridge.domain.Items;

public interface ItemRepository extends JpaRepository<Items, Long> {

    @Query(value = """
        select *
        from items i
        where instr(
              replace(lower(:rawInput), ' ', ''),
              replace(lower(i.name), ' ', '')
        ) > 0
        order by length(i.name) desc
        fetch first 1 rows only
        """, nativeQuery = true)
    Optional<Items> findBestMatchByNameContained(@Param("rawInput") String rawInput);
}
