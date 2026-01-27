package com.example.project.domain.groupbuy.controller;

import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.project.domain.groupbuy.dto.GroupBuyPostCreateRequest;
import com.example.project.domain.groupbuy.dto.GroupBuyPostResponse;
import com.example.project.domain.groupbuy.service.GroupBuyPostService;
import com.example.project.member.domain.Users;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@RestController
@RequestMapping("/api/group-buy")
@RequiredArgsConstructor
public class GroupBuyPostController {

    private final GroupBuyPostService groupBuyPostService;
    private final ObjectMapper objectMapper;

 // 1. 게시글 등록 (글 + 이미지 통합)
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Long> createPost(
            @RequestParam("postDto") String postDtoJson,
            @RequestPart(value = "files", required = false) List<MultipartFile> files,
            @AuthenticationPrincipal Users userDetails
    ) {
        try {
            // JSON 문자열을 DTO로 파싱
            GroupBuyPostCreateRequest request = objectMapper.readValue(postDtoJson, GroupBuyPostCreateRequest.class);
            // 서비스에서 글 생성과 이미지 저장을 한 번에 처리하도록 호출
            Long postId = groupBuyPostService.createPostWithImages(userDetails.getUserId(), request, files);
            return ResponseEntity.ok(postId);
        } catch (Exception e) {
            log.error("postDto JSON 파싱 실패: {}", postDtoJson, e);
            throw new IllegalArgumentException("postDto JSON 형식이 올바르지 않습니다: " + e.getMessage());
        }
    }

    // 2. 게시글 수정 (글 수정 + 이미지 교체)
    @PostMapping(value = "/{postId}/update", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> updatePost(
            @PathVariable("postId") Long postId,
            @RequestParam("postDto") String postDtoJson,
            @RequestPart(value = "files", required = false) List<MultipartFile> files,
            @AuthenticationPrincipal Users userDetails) {
        try {
            // JSON 문자열을 DTO로 파싱
            GroupBuyPostCreateRequest request = objectMapper.readValue(postDtoJson, GroupBuyPostCreateRequest.class);
            groupBuyPostService.updatePostWithImages(postId, request, files, userDetails);
            return ResponseEntity.ok("게시글 수정 완료");
        } catch (Exception e) {
            log.error("postDto JSON 파싱 실패: {}", postDtoJson, e);
            throw new IllegalArgumentException("postDto JSON 형식이 올바르지 않습니다: " + e.getMessage());
        }
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
    public ResponseEntity<GroupBuyPostResponse> getPost(
    		@AuthenticationPrincipal Users userDetails,
    		@PathVariable("postId") Long postId) {
        return ResponseEntity.ok(groupBuyPostService.getPostDetail(postId, userDetails.getUserId()));
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
    
 // 7. 내가 찜한 게시글 목록 조회
    @GetMapping("/favorites")
    public ResponseEntity<List<GroupBuyPostResponse>> getMyFavorites(
            @AuthenticationPrincipal Users userDetails
    ) {
        List<GroupBuyPostResponse> favorites = groupBuyPostService.getMyFavoritePosts(userDetails.getUserId());
        return ResponseEntity.ok(favorites);
    }
    
 // 8. 게시글 삭제 (이미지 + 글 전체)
    @DeleteMapping("/{postId}")
    public ResponseEntity<String> deletePost(
            @PathVariable("postId") Long postId,
            @AuthenticationPrincipal Users userDetails) {
        
        groupBuyPostService.deleteWholePost(postId, userDetails.getUserId());
        return ResponseEntity.ok("게시글 삭제가 완료되었습니다.");
    }
    
}
