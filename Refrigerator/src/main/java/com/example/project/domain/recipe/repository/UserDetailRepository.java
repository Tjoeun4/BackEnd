package com.example.project.domain.recipe.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.project.domain.recipe.domain.UserDetail;

public interface UserDetailRepository extends JpaRepository<UserDetail, Long> {
    // 사용자 ID로 상세 취향 정보 조회
    Optional<UserDetail> findByUserId(Long userId);
}