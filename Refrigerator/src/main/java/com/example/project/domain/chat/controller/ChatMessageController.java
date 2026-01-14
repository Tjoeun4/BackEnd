package com.example.project.domain.chat.controller;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import com.example.project.domain.chat.dto.ChatMessageRequest;
import com.example.project.domain.chat.dto.ChatMessageResponse;
import com.example.project.domain.chat.service.ChatService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class ChatMessageController {

    private final SimpMessagingTemplate messagingTemplate;
    private final ChatService chatService;

    // Flutter에서 /pub/chat/message로 메시지를 보내면 실행
    @MessageMapping("/chat/message")
    public void sendMessage(@Payload ChatMessageRequest request) {
        // 1. DB에 메시지 저장
        ChatMessageResponse response = chatService.saveMessage(request);
        
        // 2. 해당 방을 구독(/sub/chat/room/{roomId}) 중인 유저들에게 실시간 전송
        messagingTemplate.convertAndSend("/sub/chat/room/" + request.getRoomId(), response);
    }
}
