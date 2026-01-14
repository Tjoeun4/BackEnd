package com.example.project.domain.chat.repository;

import com.example.project.domain.chat.domain.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {
    // 기본 제공되는 save, findById, deleteById 사용
}