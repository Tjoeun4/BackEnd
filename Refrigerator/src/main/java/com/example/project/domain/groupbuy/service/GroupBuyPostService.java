package com.example.project.domain.groupbuy.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.project.domain.fridge.domain.FoodCategory;
import com.example.project.domain.fridge.repository.FoodCategoryRepository;
import com.example.project.domain.groupbuy.domain.GroupBuyParticipant;
import com.example.project.domain.groupbuy.domain.GroupBuyPost;
import com.example.project.domain.groupbuy.domain.PostFavorite;
import com.example.project.domain.groupbuy.dto.GroupBuyPostCreateRequest;
import com.example.project.domain.groupbuy.dto.GroupBuyPostResponse;
import com.example.project.domain.groupbuy.repository.GroupBuyParticipantRepository;
import com.example.project.domain.groupbuy.repository.GroupBuyPostRepository;
import com.example.project.domain.groupbuy.repository.PostFavoriteRepository;
import com.example.project.global.neighborhood.Neighborhood;
import com.example.project.global.neighborhood.NeighborhoodRepository;
import com.example.project.member.domain.Users;
import com.example.project.member.repository.UsersRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class GroupBuyPostService {

    private final GroupBuyPostRepository postRepository;
    // 아래 Repository들은 각 도메인 패키지에 선언되어 있다고 가정합니다.
    private final UsersRepository usersRepository;
    private final FoodCategoryRepository categoryRepository;
    private final NeighborhoodRepository neighborhoodRepository;
    private final PostFavoriteRepository favoriteRepository;
    private final GroupBuyParticipantRepository participantRepository;
    
    
    
    /**
     * 공동구매 게시글 생성
     */
    @Transactional
    public Long createPost(Long userId, GroupBuyPostCreateRequest request) {
        // 1. 관련 엔티티 조회 (존재하지 않으면 예외 발생)
        Users user = usersRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("해당 유저가 존재하지 않습니다."));
        FoodCategory category = categoryRepository.findById(request.getCategoryId())
               .orElseThrow(() -> new IllegalArgumentException("해당 카테고리가 존재하지 않습니다."));
        Neighborhood neighborhood = user.getNeighborhood();

        // 2. 게시글 엔티티 빌더를 사용하여 생성
        GroupBuyPost post = GroupBuyPost.builder()
                .user(user)
                .category(category)
                .neighborhood(neighborhood)
                .title(request.getTitle())
                .description(request.getDescription())
                .priceTotal(request.getPriceTotal())
                .meetPlaceText(request.getMeetPlaceText())
                .status("OPEN") // 기본값 설정
                .startdate(request.getStartdate())
                .enddate(request.getEnddate())
                .build();

        // 3. 저장 및 ID 반환
        return postRepository.save(post).getPostId();
    }

    /**
     * 특정 동네(시군구)의 게시글 목록 조회
     */
    public List<GroupBuyPostResponse> getPostsByNeighborhood(Long neighborhoodId) {
        return postRepository.findAllByNeighborhood_NeighborhoodIdOrderByCreatedAtDesc(neighborhoodId)
                .stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    /**
     * 게시글 상세 조회
     */
    public GroupBuyPostResponse getPostDetail(Long postId) {
        GroupBuyPost post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 존재하지 않습니다."));
        return convertToResponse(post);
    }

    /**
     * Entity -> DTO 변환 (공통 메서드)
     */
    private GroupBuyPostResponse convertToResponse(GroupBuyPost post) {
        return GroupBuyPostResponse.builder()
                .postId(post.getPostId())
                .title(post.getTitle())
                .description(post.getDescription())
                .priceTotal(post.getPriceTotal())
                .meetPlaceText(post.getMeetPlaceText())
                .status(post.getStatus())
                .categoryName(post.getCategory().getName()) // FoodCategory에 getName()이 있다고 가정
                .authorNickname(post.getUser().getNickname()) // Users에 getNickname()이 있다고 가정
                .createdAt(post.getCreatedAt())
                .build();
    }
    
    /**
     * 찜하기 토글 (찜하기 추가 또는 취소)
     */
    @Transactional
    public String toggleFavorite(Long userId, Long postId) {
        Users user = usersRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("해당 유저가 존재하지 않습니다."));
        GroupBuyPost post = postRepository.findByPostId(postId)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 존재하지 않습니다."));

        // 이미 찜했는지 확인
        return favoriteRepository.findByUserAndPost(user, post)
                .map(favorite -> {
                    // 이미 존재하면 삭제 (취소)
                    favoriteRepository.delete(favorite);
                    return "찜하기 취소 완료";
                })
                .orElseGet(() -> {
                    // 존재하지 않으면 생성 (추가)
                    PostFavorite favorite = PostFavorite.builder()
                            .user(user)
                            .post(post)
                            .build();
                    favoriteRepository.save(favorite);
                    return "찜하기 완료";
                });
    }
    
    /**
     * 키워드 및 동네 기반 게시글 검색
     */
    public List<GroupBuyPostResponse> searchPosts(String keyword, Long neighborhoodId) {
        // 키워드가 비어있을 경우 전체 검색 혹은 예외 처리 로직 추가 가능
        return postRepository.searchByKeywordAndNeighborhood(keyword, neighborhoodId)
                .stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }
    /**
     * 특정 동네 내에서 카테고리별로 게시글 필터링 조회
     */
    public List<GroupBuyPostResponse> getPostsByCategory(Long neighborhoodId, Long categoryId) {
        // Repository에 이미 정의된 메서드 활용
        return postRepository.findAllByNeighborhood_NeighborhoodIdAndCategory_CategoryId(neighborhoodId, categoryId)
                .stream()
                .map(this::convertToResponse) // 기존에 만든 변환 메서드 재사용
                .collect(Collectors.toList());
    }
    
    
    
    @Transactional
    public String joinGroupBuy(Long userId, Long postId) {
        // 1. 엔티티 조회
        Users user = usersRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("해당 유저가 존재하지 않습니다."));
        GroupBuyPost post = postRepository.findByPostId(postId)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 존재하지 않습니다."));

        // 2. 검증: 본인 글 참여 불가
        if (post.getUser().getUserId().equals(userId)) {
            return "본인이 작성한 글에는 참여할 수 없습니다.";
        }

        // 3. 검증: 중복 참여 확인
        if (participantRepository.existsByUserAndPost(user, post)) {
            return "이미 참여 중인 공동구매입니다.";
        }

        // 4. 검증: 인원 마감 확인
        if (post.getCurrentParticipants() >= post.getMaxParticipants()) {
            return "모집 인원이 마감되었습니다.";
        }

        // 5. 참여 정보 저장
        GroupBuyParticipant participant = GroupBuyParticipant.builder()
                .user(user)
                .post(post)
                .joinedAt(LocalDateTime.now())
                .build();
        
        participantRepository.save(participant);
        post.incrementParticipants(); // 인원 수 +1

        // 6. 만석 시 상태 변경
        if (post.getCurrentParticipants() == post.getMaxParticipants()) {
            post.updateStatus("CLOSED");
        }

        return "참여 신청이 완료되었습니다.";
    }
    
    /**
     * 내가 찜한 공동구매 게시글 목록 조회
     */
    public List<GroupBuyPostResponse> getMyFavoritePosts(Long userId) {
        // 1. 유저 조회
        Users user = usersRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("해당 유저가 존재하지 않습니다."));

        // 2. 찜 내역 조회 및 DTO 변환
        return favoriteRepository.findAllByUserOrderByPost_CreatedAtDesc(user)
                .stream()
                .map(favorite -> convertToResponse(favorite.getPost())) // Favorite 엔티티에서 Post를 꺼내 변환
                .collect(Collectors.toList());
    }
}
