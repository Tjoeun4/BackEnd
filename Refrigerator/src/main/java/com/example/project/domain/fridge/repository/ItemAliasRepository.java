package com.example.project.domain.fridge.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.project.domain.fridge.domain.ItemAlias;

public interface ItemAliasRepository extends JpaRepository<ItemAlias, Long> {
    Optional<ItemAlias> findByAlias(String alias);
    boolean existsByAlias(String alias);
}
