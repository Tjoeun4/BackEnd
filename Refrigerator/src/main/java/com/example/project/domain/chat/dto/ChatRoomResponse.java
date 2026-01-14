package com.example.project.domain.chat.dto;

import java.time.LocalDateTime;

import com.example.project.domain.chat.domain.ChatRoom;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ChatRoomResponse {
	private Long roomId;
    private String roomName;
    private ChatRoomType type;

    // ChatRoomMember 엔티티에서 ChatRoom 정보를 추출하여 생성하도록 수정
    public static ChatRoomResponse from(com.example.project.domain.chat.domain.ChatRoomMember member) {
        ChatRoom room = member.getRoom();
        return ChatRoomResponse.builder()
                .roomId(room.getRoomId())
                .roomName(room.getRoomName())
                .type(room.getType())
                .build();
    }
}
