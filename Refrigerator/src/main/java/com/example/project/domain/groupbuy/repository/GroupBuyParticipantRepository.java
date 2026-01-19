package com.example.project.domain.groupbuy.repository;

import com.example.project.domain.groupbuy.domain.GroupBuyPost;
import com.example.project.domain.groupbuy.domain.GroupBuyParticipant;
import com.example.project.member.domain.Users;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GroupBuyParticipantRepository extends JpaRepository<GroupBuyParticipant, Long> {
    // 특정 유저가 해당 게시글에 이미 참여했는지 확인
    boolean existsByUserAndPost(Users user, GroupBuyPost post);
}
