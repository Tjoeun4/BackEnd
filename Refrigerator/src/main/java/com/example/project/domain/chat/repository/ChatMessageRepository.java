package com.example.project.domain.chat.repository;

import com.example.project.domain.chat.domain.ChatMessage;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {

    // 특정 채팅방의 모든 메시지를 시간순으로 조회
    // 정렬 기준은 BaseTimeEntity의 생성 시간(createdAt)을 사용합니다.
    List<ChatMessage> findAllByRoomRoomIdOrderByCreatedAtAsc(Long roomId);
    Slice<ChatMessage> findAllByRoomRoomIdOrderByCreatedAtDesc(Long roomId, Pageable pageable);
	
    List<ChatMessage> findByRoomRoomIdOrderByCreatedAtAsc(Long roomId);
 // 미래의 업그레이드 버전 예시
 // Slice<ChatMessage> findByRoomRoomId(Long roomId, Pageable pageable);
    
    
    // (선택) 최신 메시지 50개만 가져오기 (성능 최적화용)
    // List<ChatMessage> findTop50ByRoomRoomIdOrderByCreatedAtDesc(Long roomId);
}
