package com.example.project.domain.groupbuy.controller;

import com.example.project.domain.groupbuy.dto.GroupBuyPostCreateRequest;
import com.example.project.domain.groupbuy.dto.GroupBuyPostResponse;
import com.example.project.domain.groupbuy.service.GroupBuyPostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/group-buy")
@RequiredArgsConstructor
public class GroupBuyPostController {

    private final GroupBuyPostService groupBuyPostService;

    // 1. 게시글 등록
    @PostMapping
    public ResponseEntity<Long> createPost(@RequestBody GroupBuyPostCreateRequest request) {
        // 실제 운영 시에는 SecurityContext에서 userId를 가져오겠지만, 
        // 테스트를 위해 임시로 1L을 넘깁니다.
        Long postId = groupBuyPostService.createPost(1L, request);
        return ResponseEntity.ok(postId);
    }

    // 2. 동네별 목록 조회
    @GetMapping
    public ResponseEntity<List<GroupBuyPostResponse>> getPosts(@RequestParam Long neighborhoodId) {
        List<GroupBuyPostResponse> posts = groupBuyPostService.getPostsByNeighborhood(neighborhoodId);
        return ResponseEntity.ok(posts);
    }

    // 3. 상세 조회
    @GetMapping("/{postId}")
    public ResponseEntity<GroupBuyPostResponse> getPost(@PathVariable Long postId) {
        return ResponseEntity.ok(groupBuyPostService.getPostDetail(postId));
    }

    // 4. 찜하기 토글
    @PostMapping("/{postId}/favorite")
    public ResponseEntity<String> toggleFavorite(@PathVariable Long postId) {
        // 임시 유저 ID 사용
        String message = groupBuyPostService.toggleFavorite(1L, postId);
        return ResponseEntity.ok(message);
    }
}
