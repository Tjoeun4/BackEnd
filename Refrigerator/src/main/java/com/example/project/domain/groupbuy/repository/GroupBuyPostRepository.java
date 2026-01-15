package com.example.project.domain.groupbuy.repository;

import com.example.project.domain.groupbuy.domain.GroupBuyPost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface GroupBuyPostRepository extends JpaRepository<GroupBuyPost, Long> {
    
    // 특정 시군구(Neighborhood)의 게시글만 최신순으로 조회
    List<GroupBuyPost> findAllByNeighborhood_IdOrderByCreatedAtDesc(Long neighborhoodId);
    
    // 특정 시군구 내에서 카테고리별 필터링
    List<GroupBuyPost> findAllByNeighborhood_IdAndCategory_Id(Long neighborhoodId, Long categoryId);
}