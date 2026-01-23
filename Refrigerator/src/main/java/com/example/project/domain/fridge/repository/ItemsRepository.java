package com.example.project.domain.fridge.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.project.domain.fridge.domain.Items;

import java.util.List;

public interface ItemsRepository extends JpaRepository<Items, Long> {
    List<Items> findByNameContaining(String name);
}
