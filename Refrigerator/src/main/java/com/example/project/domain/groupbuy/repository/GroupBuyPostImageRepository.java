package com.example.project.domain.groupbuy.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.project.domain.groupbuy.domain.GroupBuyPost;
import com.example.project.domain.groupbuy.domain.GroupBuyPostImage;

public interface GroupBuyPostImageRepository extends JpaRepository<GroupBuyPostImage, Long> {
    // 특정 게시글의 이미지를 모두 지울 때 필요
    void deleteByPost(GroupBuyPost post);

	List<GroupBuyPostImage> findByPost(GroupBuyPost post);
}
