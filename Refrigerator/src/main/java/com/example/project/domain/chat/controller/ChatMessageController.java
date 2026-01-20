package com.example.project.domain.chat.controller;

import java.util.List;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.project.domain.chat.dto.ChatMessageRequest;
import com.example.project.domain.chat.dto.ChatMessageResponse;
import com.example.project.domain.chat.service.ChatService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class ChatMessageController {

    private final SimpMessagingTemplate messagingTemplate;
    private final ChatService chatService;

    /**
     * 1. 실시간 메시지 전송 (기존 유지 및 보완)
     * 목적지: /pub/chat/message
     */
    @MessageMapping("/chat/message")
    public void sendMessage(@Payload ChatMessageRequest request) {
        // DB 저장 (이미 구현하신 서비스 호출)
        ChatMessageResponse response = chatService.saveMessage(request);
        
        // 해당 방을 구독 중인 모든 유저에게 메시지 뿌리기
        // 구독 경로: /sub/chat/room/{roomId}
        messagingTemplate.convertAndSend("/sub/chat/room/" + request.getRoomId(), response);
    }

    /**
     * 2. [추가] 과거 채팅 내역 불러오기 (REST API 연동)
     * 앱에서 방 입장 시 "가장 먼저" 호출해야 함
     */
    @GetMapping("/api/chat/room/{roomId}/messages")
    @ResponseBody // 일반 HTTP 응답을 위해 추가
    public List<ChatMessageResponse> getMessages(@PathVariable Long roomId) {
        // 서비스에서 DB 데이터를 최신순으로 가져옴
        return chatService.getMessages(roomId);
    }

    /**
     * 3. [추가] 채팅방 입장 알림 (선택 사항)
     * 누군가 들어왔을 때 "누구님이 입장했습니다"라고 방 인원에게 알림
     */
    @MessageMapping("/chat/enter")
    public void enterRoom(@Payload ChatMessageRequest request) {
        request.setContent(request.getSenderId() + "님이 입장하셨습니다.");
        request.setType("ENTER"); // 타입을 ENTER로 지정
        
        // 입장은 DB에 저장할 수도 있고, 그냥 실시간 알림만 줄 수도 있음
        messagingTemplate.convertAndSend("/sub/chat/room/" + request.getRoomId(), request);
    }
}
