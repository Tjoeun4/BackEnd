package com.example.project.security.config;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.test.web.servlet.MockMvc;

import com.example.project.domain.chat.controller.ChatRoomController;
import com.example.project.domain.chat.service.ChatRoomService;
import com.example.project.domain.chat.service.ChatService;
import com.example.project.domain.groupbuy.controller.GroupBuyPostController;
import com.example.project.domain.groupbuy.service.GroupBuyPostService;
import com.example.project.global.hashid.HashidToLongConverter;
import com.example.project.member.domain.Users;
import com.example.project.security.token.TokenRepository;

/**
 * SecurityConfiguration의 인가 규칙이 의도대로 적용되는지 검증한다.
 * 채팅·공동구매 API는 "반드시 인증 필요"로 설계됐으므로, 토큰 없는 요청은 컨트롤러에 닿기 전에 막혀야 한다.
 */
@WebMvcTest(controllers = { ChatRoomController.class, GroupBuyPostController.class })
@Import(SecurityConfiguration.class)
class SecurityRuleOrderTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ChatRoomService chatRoomService;

    @MockBean
    private ChatService chatService;

    @MockBean
    private GroupBuyPostService groupBuyPostService;

    // SecurityConfiguration·JwtAuthenticationFilter·WebConfig가 요구하는 빈
    @MockBean
    private JwtService jwtService;

    @MockBean
    private UserDetailsService userDetailsService;

    @MockBean
    private TokenRepository tokenRepository;

    @MockBean
    private AuthenticationProvider authenticationProvider;

    @MockBean
    private LogoutHandler logoutHandler;

    @MockBean
    private HashidToLongConverter hashidToLongConverter;

    // 메인 클래스의 @EnableJpaAuditing이 웹 슬라이스에서도 JPA 메타모델을 찾기 때문에 대신 넣는다
    @MockBean
    private JpaMetamodelMappingContext jpaMetamodelMappingContext;

    @Test
    void 비로그인_사용자의_채팅방_목록_요청은_막힌다() throws Exception {
        mockMvc.perform(get("/api/chat/rooms"))
                .andExpect(status().isForbidden());
    }

    @Test
    void 비로그인_사용자의_공구_찜목록_요청은_막힌다() throws Exception {
        mockMvc.perform(get("/api/group-buy/favorites"))
                .andExpect(status().isForbidden());
    }

    @Test
    void 로그인_사용자의_채팅방_목록_요청은_통과한다() throws Exception {
        when(chatService.getMyRooms(anyLong())).thenReturn(List.of());

        mockMvc.perform(get("/api/chat/rooms").with(user(loginUser())))
                .andExpect(status().isOk());
    }

    private Users loginUser() {
        return Users.builder()
                .userId(1L)
                .email("user@test.local")
                .build();
    }
}
