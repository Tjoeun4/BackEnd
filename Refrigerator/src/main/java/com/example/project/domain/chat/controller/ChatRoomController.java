package com.example.project.domain.chat.controller;


import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
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

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/chat")
@RequiredArgsConstructor
public class ChatRoomController {

    private final ChatRoomService chatRoomService;
    private final ChatService chatService;

    
 // 중복된 @PostMapping("/room/personal") 메서드가 있다면 하나만 남기고 지워야 합니다.
    @PostMapping("/room/personal")
    public ResponseEntity<Long> createPersonalRoom(
            @RequestParam Long userId,
            @RequestBody ChatRoomRequest request) {
        Long roomId = chatRoomService.createPersonalChatRoom(userId, request.getRoomName(), request.getType());
        return ResponseEntity.ok(roomId);
    }

    /**
     * 2. 공구/나눔 채팅방 생성 (게시글 기반)
     * POST /api/chat/room/group-buy/{postId}
     */
//    @PostMapping("/room/group-buy/{postId}")
//    public ResponseEntity<Long> createGroupBuyRoom(
//            @RequestParam Long userId,
//            @PathVariable Long postId) {
//        Long roomId = chatRoomService.createGroupBuyChatRoom(userId, postId);
//        return ResponseEntity.ok(roomId);
//    }

    /**
     * 3. 내가 참여 중인 채팅방 목록 조회
     */
    @GetMapping("/rooms")
    public ResponseEntity<List<ChatRoomResponse>> getMyRooms(@RequestParam Long userId) {
        return ResponseEntity.ok(chatService.getMyRooms(userId));
    }

    /**
     * 4. 채팅방 상세 및 과거 내역 조회 (페이징 적용)
     * GET /api/chat/room/{roomId}?userId=1&page=0&size=20
     */
    @GetMapping("/room/{roomId}")
    public ResponseEntity<ChatHistoryResponse> getChatHistory(
            @PathVariable Long roomId,
            @RequestParam Long userId,
            @PageableDefault(size = 30) Pageable pageable) {
        // 방 입장 시 읽음 처리 로직 포함
        return ResponseEntity.ok(chatService.getChatHistory(roomId, userId, pageable));
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
