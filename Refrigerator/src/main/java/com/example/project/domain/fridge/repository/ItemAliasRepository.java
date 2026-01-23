package com.example.project.domain.fridge.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.project.domain.fridge.domain.ItemAlias;

import java.util.Optional;

public interface ItemAliasRepository extends JpaRepository<ItemAlias, Long> {
    Optional<ItemAlias> findByRawName(String rawName);
    boolean existsByRawName(String rawName);
}
