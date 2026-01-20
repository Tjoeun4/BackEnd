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
    	System.out.println(">>> STOMP 메시지 수신 성공! 내용: " + request.getContent());
        // 1. DB에 메시지 저장
        ChatMessageResponse response = chatService.saveMessage(request);
        
        // 2. 해당 방을 구독(/sub/chat/room/{roomId}) 중인 유저들에게 실시간 전송
        messagingTemplate.convertAndSend("/sub/chat/room/" + request.getRoomId(), response);
    }
    
    @MessageMapping("/chat/message1") // 주소도 message1로 변경
    public void sendMessage1(String message) {
        System.out.println("================================");
        System.out.println(">>> [테스트] STOMP 입구 도달!");
        System.out.println(">>> 데이터 원문: " + message);
        System.out.println("================================");
    }
}
