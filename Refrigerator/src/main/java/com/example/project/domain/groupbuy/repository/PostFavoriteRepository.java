package com.example.project.domain.groupbuy.repository;

import com.example.project.domain.groupbuy.domain.GroupBuyPost;
import com.example.project.domain.groupbuy.domain.PostFavorite;
import com.example.project.member.domain.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PostFavoriteRepository extends JpaRepository<PostFavorite, Long> {
    Optional<PostFavorite> findByUserAndPost(Users user, GroupBuyPost post);
    
    // 유저가 찜한 게시글 목록을 가져올 때 사용
    // List<PostFavorite> findAllByUser(Users user);
}