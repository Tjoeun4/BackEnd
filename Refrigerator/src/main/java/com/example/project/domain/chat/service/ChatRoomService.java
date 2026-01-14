package com.example.project.domain.chat.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.project.domain.chat.domain.ChatRoom;
import com.example.project.domain.chat.domain.ChatRoomMember;
import com.example.project.domain.chat.dto.ChatRoomType;
import com.example.project.domain.chat.dto.MemberRole;
import com.example.project.domain.chat.repository.ChatRoomMemberRepository;
import com.example.project.domain.chat.repository.ChatRoomRepository;
import com.example.project.member.domain.Users;
import com.example.project.member.repository.UsersRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ChatRoomService {

    private final ChatRoomRepository chatRoomRepository;
    private final ChatRoomMemberRepository chatRoomMemberRepository;
    private final UsersRepository usersRepository;
    // private final GroupBuyPostRepository postRepository;

    /**
     * 1. 일반 채팅방 생성 (PERSONAL, FAMILY 등)
     * 친구 목록에서 대화하기를 누르거나 가족 방을 만들 때 사용
     */
    @Transactional
    public Long createPersonalChatRoom(Long userId, String roomName, ChatRoomType type) {
        Users creator = findUserById(userId);

        ChatRoom chatRoom = ChatRoom.builder()
                .type(type) // PERSONAL 또는 FAMILY
                .roomName(roomName)
                .build();
        chatRoomRepository.save(chatRoom);

        saveChatRoomMember(creator, chatRoom, MemberRole.OWNER);

        return chatRoom.getRoomId();
    }

    /**
     * 2. 공구/나눔 전용 채팅방 생성 (GROUP_BUY)
     * 게시글에서 '채팅하기'를 눌렀을 때 게시글 정보와 연동하여 생성
     */
//    @Transactional
//    public Long createGroupBuyChatRoom(Long userId, Long postId) {
//        Users creator = findUserById(userId);
//        GroupBuyPost post = postRepository.findById(postId)
//                .orElseThrow(() -> new RuntimeException("게시글을 찾을 수 없습니다."));
//
//        // 공구 방은 게시글 제목을 기본 방 이름으로 설정
//        ChatRoom chatRoom = ChatRoom.builder()
//                .type(ChatRoomType.GROUP_BUY)
//                .post(post)
//                .roomName("[공구] " + post.getTitle())
//                .build();
//        chatRoomRepository.save(chatRoom);
//
//        // 방장 등록
//        saveChatRoomMember(creator, chatRoom, MemberRole.OWNER);
//        
//        // 추가 로직: 게시글 작성자(판매자)도 자동으로 이 방에 참여시켜야 한다면 여기서 추가
//        // saveChatRoomMember(post.getSeller(), chatRoom, MemberRole.PARTICIPANT);
//
//        return chatRoom.getRoomId();
//    }

    // 공통 로직: 사용자 조회
    private Users findUserById(Long userId) {
        return usersRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));
    }

    // 공통 로직: 멤버 등록
    private void saveChatRoomMember(Users user, ChatRoom room, MemberRole role) {
        ChatRoomMember member = ChatRoomMember.builder()
                .user(user)
                .room(room)
                .role(role)
                .build();
        chatRoomMemberRepository.save(member);
    }
}
