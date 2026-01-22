package com.example.project.domain.groupbuy.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.project.domain.groupbuy.domain.GroupBuyPost;
import com.example.project.domain.groupbuy.domain.PostFavorite;
import com.example.project.member.domain.Users;

public interface PostFavoriteRepository extends JpaRepository<PostFavorite, Long> {
    Optional<PostFavorite> findByUserAndPost(Users user, GroupBuyPost post);
    
    // 유저가 찜한 게시글 목록을 가져올 때 사용
    // List<PostFavorite> findAllByUser(Users user);
    
    // 유저가 찜한 내역을 리스트로 조회 (최신순 정렬을 원한다면 추가 옵션 가능)
    List<PostFavorite> findAllByUserOrderByPost_CreatedAtDesc(Users user);
    
 // 추가: 찜 여부 확인을 위한 메서드
    boolean existsByUserAndPost(Users user, GroupBuyPost post);
    

    // 유저가 찜한 게시글 ID 목록 조회 (목록용 - N+1 예방)
    @Query("SELECT f.post.postId FROM PostFavorite f WHERE f.user = :user AND f.post.postId IN :postIds")
    List<Long> findFavoritePostIdsByUserAndPostIds(@Param("user") Users user, @Param("postIds") List<Long> postIds);

    boolean existsByUser_UserIdAndPost_PostId(Long userId, Long postId);

}