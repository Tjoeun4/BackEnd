package com.example.project.global.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.*;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // Flutter에서 연결할 엔드포인트: ws://서버주소:8080/ws-stomp
        registry.addEndpoint("/ws-stomp")
                .setAllowedOriginPatterns("*"); // 테스트를 위해 모든 오리진 허용
        // 보안상 특정 도메인만 허용해야 하지만, 개발 중에는 Flutter 앱이나 웹에서 자유롭게 접근할 수 있도록 모든 경로를 허용하는 설정입니다.
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        // 메시지를 받을 때(구독): /sub/chat/room/1
        registry.enableSimpleBroker("/sub");
        // 메시지를 보낼 때(발행): /pub/chat/message
        registry.setApplicationDestinationPrefixes("/pub");
    }
}