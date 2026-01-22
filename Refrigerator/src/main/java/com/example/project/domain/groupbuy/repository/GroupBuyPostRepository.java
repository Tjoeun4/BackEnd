package com.example.project.domain.groupbuy.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.project.domain.groupbuy.domain.GroupBuyPost;


@Repository
public interface GroupBuyPostRepository extends JpaRepository<GroupBuyPost, Long> {
    
    // 특정 시군구(Neighborhood)의 게시글만 최신순으로 조회
	List<GroupBuyPost> findAllByNeighborhood_NeighborhoodIdOrderByCreatedAtDesc(Long neighborhoodId);   
	
    // 특정 시군구 내에서 카테고리별 필터링
	List<GroupBuyPost> findAllByNeighborhood_NeighborhoodIdAndCategory_CategoryId(Long neighborhoodId, Long categoryId);	
	
	
	// 기존 코드에 아래 메서드 추가
	// 제목 키워드 포함 + 특정 시군구 ID 조건
	List<GroupBuyPost> findByTitleContainingAndNeighborhood_NeighborhoodIdOrderByCreatedAtDesc(String title, Long neighborhoodId);
	
	
	Optional<GroupBuyPost> findByPostId(Long postId);

	@Query("SELECT p FROM GroupBuyPost p WHERE " +
	           "(:keyword IS NULL OR p.title LIKE %:keyword% OR p.description LIKE %:keyword%) " + 
	           "AND (:neighborhoodId IS NULL OR p.neighborhood.neighborhoodId = :neighborhoodId) " +
	           "ORDER BY p.postId DESC") // createdAt 대신 명확한 postId 정렬 사용 가능
	    List<GroupBuyPost> searchByKeywordAndNeighborhood(
	        @Param("keyword") String keyword, 
	        @Param("neighborhoodId") Long neighborhoodId
	    );
}