package com.example.project.domain.chat.controller;


import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.project.domain.chat.dto.ChatHistoryResponse;
import com.example.project.domain.chat.dto.ChatRoomRequest;
import com.example.project.domain.chat.dto.ChatRoomResponse;
import com.example.project.domain.chat.service.ChatRoomService;
import com.example.project.domain.chat.service.ChatService;
import com.example.project.member.domain.CustomUserDetails;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/chat")
@RequiredArgsConstructor
public class ChatRoomController {

    private final ChatRoomService chatRoomService;
    private final ChatService chatService;

    
 // 1. 개인 채팅방 생성 (내 ID는 토큰에서, 상대 ID만 받음)
    @PostMapping("/room/personal")
    public ResponseEntity<Long> createPersonalRoom(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestParam Long targetId) { // roomName 대신 상대방 targetId를 받는 것이 더 명확함
        Long roomId = chatRoomService.createPersonalChatRoom(userDetails.getUserId(), targetId);
        return ResponseEntity.ok(roomId);
    }

 // 2. 공구 채팅방 참여/생성 (게시글 기반)
    @PostMapping("/room/group-buy/{postId}")
    public ResponseEntity<Long> createGroupBuyRoom(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Long postId) {
        Long roomId = chatRoomService.createGroupBuyChatRoom(userDetails.getUserId(), postId);
        return ResponseEntity.ok(roomId);
    }

    /**
     * 3. 내가 참여 중인 채팅방 목록 조회
     */
    @GetMapping("/rooms")
    public ResponseEntity<List<ChatRoomResponse>> getMyRooms(
    		@AuthenticationPrincipal CustomUserDetails userDetails
    		) {
        return ResponseEntity.ok(chatService.getMyRooms(userDetails.getUserId()));
    }

    /**
     * 4. 채팅방 상세 및 과거 내역 조회 (페이징 적용)
     * GET /api/chat/room/{roomId}?userId=1&page=0&size=20
     */
    @GetMapping("/room/{roomId}")
    public ResponseEntity<ChatHistoryResponse> getChatHistory(
            @PathVariable Long roomId,
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PageableDefault(size = 30) Pageable pageable) {
        // 방 입장 시 읽음 처리 로직 포함
        return ResponseEntity.ok(chatService.getChatHistory(roomId, userDetails.getUserId(), pageable));
    }

    /**
     * 5. 채팅방 나가기
     */
    @DeleteMapping("/room/{roomId}/leave")
    public ResponseEntity<Void> leaveRoom(
            @PathVariable Long roomId,
            @RequestParam Long userId) {
        chatService.leaveRoom(roomId, userId);
        return ResponseEntity.noContent().build();
    }
}
