package com.example.project.domain.groupbuy.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.project.domain.groupbuy.dto.GroupBuyPostCreateRequest;
import com.example.project.domain.groupbuy.dto.GroupBuyPostResponse;
import com.example.project.domain.groupbuy.service.GroupBuyPostService;
import com.example.project.member.domain.Users;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/group-buy")
@RequiredArgsConstructor
public class GroupBuyPostController {

    private final GroupBuyPostService groupBuyPostService;

    // 1. 게시글 등록
    @PostMapping
    public ResponseEntity<Long> createPost(
    		@RequestBody GroupBuyPostCreateRequest request,
            @AuthenticationPrincipal Users userDetails // 1. 보안 객체로 변경
    		) {
    	Long currentUserId = userDetails.getUserId();
        Long postId = groupBuyPostService.createPost(currentUserId, request);
        return ResponseEntity.ok(postId);
    }

    // 2. 동네별 목록 조회
    @GetMapping
    public ResponseEntity<List<GroupBuyPostResponse>> getPosts(
    		@AuthenticationPrincipal Users userDetails
    		) {
        List<GroupBuyPostResponse> posts = groupBuyPostService.getPostsByNeighborhood(userDetails.getNeighborhood().getNeighborhoodId());
        return ResponseEntity.ok(posts);
    }

    // 3. 상세 조회
    @GetMapping("/{postId}")
    public ResponseEntity<GroupBuyPostResponse> getPost(@PathVariable("postId") Long postId) {
        return ResponseEntity.ok(groupBuyPostService.getPostDetail(postId));
    }

    // 4. 찜하기 토글
    @PostMapping("/{postId}/favorite")
    public ResponseEntity<String> toggleFavorite(
    		@PathVariable("postId") Long postId,
    		@AuthenticationPrincipal Users userDetails
    		) {
        // 임시 유저 ID 사용
        String message = groupBuyPostService.toggleFavorite(userDetails.getUserId(), postId);
        return ResponseEntity.ok(message);
    }
    
 // 5. 키워드 검색 (동네 필터 포함)
    @GetMapping("/search")
    public ResponseEntity<List<GroupBuyPostResponse>> searchPosts(
            @RequestParam("keyword") String keyword,
    		@AuthenticationPrincipal Users userDetails
            
    		) {
        List<GroupBuyPostResponse> results = groupBuyPostService.searchPosts(keyword, userDetails.getNeighborhood().getNeighborhoodId());
        return ResponseEntity.ok(results);
    }
    
 // 6. 동네별 + 카테고리별 필터링 조회
    @GetMapping("/filter")
    public ResponseEntity<List<GroupBuyPostResponse>> getPostsByCategory(
    		@AuthenticationPrincipal Users userDetails,
    		@RequestParam("categoryId") Long categoryId) {
        List<GroupBuyPostResponse> posts = groupBuyPostService.getPostsByCategory(userDetails.getNeighborhood().getNeighborhoodId(), categoryId);
        return ResponseEntity.ok(posts);
    }
    

    
 // 수정된 공동구매 참여 API
    @PostMapping("/{postId}/join")
    public ResponseEntity<String> joinGroupBuy(
        @AuthenticationPrincipal Users userDetails, // 1. 보안 객체로 변경
        @PathVariable("postId") Long postId
    ) {
        // 2. userDetails에서 안전하게 userId를 추출
        Long currentUserId = userDetails.getUserId();
        
        // 3. 서비스 로직에 현재 로그인한 유저 ID 전달
        String message = groupBuyPostService.joinGroupBuy(currentUserId, postId);
        
        return ResponseEntity.ok(message);
    }
    
}
