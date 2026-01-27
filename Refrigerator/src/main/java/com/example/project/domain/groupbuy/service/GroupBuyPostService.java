package com.example.project.domain.groupbuy.service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.example.project.domain.fridge.domain.FoodCategory;
import com.example.project.domain.fridge.repository.FoodCategoryRepository;
import com.example.project.domain.groupbuy.domain.GroupBuyParticipant;
import com.example.project.domain.groupbuy.domain.GroupBuyPost;
import com.example.project.domain.groupbuy.domain.GroupBuyPostImage;
import com.example.project.domain.groupbuy.domain.PostFavorite;
import com.example.project.domain.groupbuy.dto.GroupBuyPostCreateRequest;
import com.example.project.domain.groupbuy.dto.GroupBuyPostResponse;
import com.example.project.domain.groupbuy.repository.GroupBuyParticipantRepository;
import com.example.project.domain.groupbuy.repository.GroupBuyPostImageRepository;
import com.example.project.domain.groupbuy.repository.GroupBuyPostRepository;
import com.example.project.domain.groupbuy.repository.PostFavoriteRepository;
import com.example.project.global.image.ImageUploadResponse;
import com.example.project.global.image.S3ImageService;
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
    
    // 이미지 처리를 위한 S3 서비스와 repo
    private final S3ImageService s3ImageService;
    private final GroupBuyPostImageRepository imageRepository;
    
    /**
     * 공동구매 게시글 생성
     */
    @Transactional
    public Long createPost(Long userId, GroupBuyPostCreateRequest request) {
        // 1. 관련 엔티티 조회 (존재하지 않으면 예외 발생)
        Users user = usersRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("해당 유저가 존재하지 않습니다."));
        FoodCategory category = categoryRepository.findById(request.getCategoryId())
               .orElseThrow(() -> new IllegalArgumentException("해당 카테고리가 존재하지 않습니다. categoryId: " + request.getCategoryId()));
        Neighborhood neighborhood = user.getNeighborhood();
        
        // neighborhood null 체크
        if (neighborhood == null) {
            throw new IllegalStateException("사용자의 동네(neighborhood) 정보가 설정되지 않았습니다. 사용자 ID: " + userId);
        }

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
                .lat(request.getLat())
                .lng(request.getLng())
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
    public GroupBuyPostResponse getPostDetail(Long postId, Long userId) {
        GroupBuyPost post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 존재하지 않습니다."));
        boolean favorite = checkIsFavoriteById(postId, userId);
        GroupBuyPostResponse id = convertToResponse(post);
        id.setFavorite(favorite);
        return id;
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
    
    /**
     * [함수 1] 단건 찜 여부 확인 로직
     * 상세 페이지 조회 등에 사용됩니다.
     */
    private boolean checkIsFavorite(Users user, GroupBuyPost post) {
        if (user == null) return false;
        return favoriteRepository.existsByUserAndPost(user, post);
    }

    /**
     * ID 기반 찜 여부 확인 (최적화 버전)
     */
    private boolean checkIsFavoriteById(Long userId, Long postId) {
        if (userId == null || postId == null) return false;
        return favoriteRepository.existsByUser_UserIdAndPost_PostId(userId, postId);
    }
    /**
     * [함수 2] 목록 찜 여부 확인 로직 (ID Set 반환)
     * 목록 조회 시 루프 안에서 쿼리가 나가는 것을 방지합니다.
     */
    private Set<Long> getFavoritePostIds(Users user, List<GroupBuyPost> posts) {
        if (user == null || posts.isEmpty()) return Collections.emptySet();
        
        List<Long> postIds = posts.stream()
                .map(GroupBuyPost::getPostId)
                .collect(Collectors.toList());
        
        return new HashSet<>(favoriteRepository.findFavoritePostIdsByUserAndPostIds(user, postIds));
    }
    


    
    @Transactional
    public void savePostImages(List<MultipartFile> files, Long postId, Users user) {
        if (files == null || files.isEmpty()) return;

        GroupBuyPost post = postRepository.findByPostId(postId)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 존재하지 않습니다."));

        for (int i = 0; i < files.size(); i++) {
            // 1. S3 업로드 실행 (공용 서비스 호출)
            ImageUploadResponse res = s3ImageService.upload(files.get(i), "groupbuy");

            // 2. GroupBuyPostImage 엔티티 생성 (내부에 ImageInfo 포함됨)
            GroupBuyPostImage postImage = GroupBuyPostImage.builder()
                    .res(res)
                    .post(post)
                    .user(user)
                    .sortOrder(i + 1)
                    .build();

            // 3. DB 저장
            imageRepository.save(postImage);
        }
    }
    
 // [DELETE] 특정 게시글의 모든 이미지 삭제 (게시글 삭제 시 호출)
    @Transactional
    public void deletePostImagesOnly(Long postId) {
    	GroupBuyPost post = postRepository.findByPostId(postId)
    			.orElseThrow(() -> new IllegalArgumentException("해당 게시글이 존재하지 않습니다."));

    	List<GroupBuyPostImage> images = imageRepository.findByPost(post);
        

        // 1. S3에서 실제 파일들 삭제
        for (GroupBuyPostImage img : images) {
            s3ImageService.delete(img.getImageInfo().getS3Key());
        }
        
        // 2. DB 데이터 삭제
        imageRepository.deleteAllInBatch(images);
    }

 // [게시글 삭제 함수 추가]
    @Transactional
    public void deleteWholePost(Long postId, Long userId) {
        GroupBuyPost post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("게시글이 없습니다."));

        // 권한 체크 (본인 글인지 확인)
        if (!post.getUser().getUserId().equals(userId)) {
            throw new RuntimeException("본인 게시글만 삭제할 수 있습니다.");
        }

        // 1. 이미지 먼저 싹 지우기 (위에서 만든 함수 호출)
        deletePostImagesOnly(postId);

        // 2. 게시글 삭제 (이때 참여자 정보 등도 Cascade에 의해 같이 지워짐)
        postRepository.delete(post);
    }
    
    // [UPDATE] 이미지 수정 (기존 거 지우고 새 거 올리기 방식)
    @Transactional
    public void updatePostImages(List<MultipartFile> newFiles, Long postId, Users user) {
        // 1. 기존 이미지 싹 정리 (S3 + DB)
    	deletePostImagesOnly(postId);
        
        // 2. 새로운 이미지 업로드 및 저장 (기존에 만든 savePostImages 재사용)
    	savePostImages(newFiles, postId, user);
    }
    
    /**
     * [CREATE] 공동구매 게시글 및 이미지 통합 생성
     */
    @Transactional
    public Long createPostWithImages(Long userId, GroupBuyPostCreateRequest request, List<MultipartFile> files) {
        // 1. 기존 게시글 생성 로직 호출
        Long postId = createPost(userId, request);
        
        // 2. 이미지가 있다면 저장
        if (files != null && !files.isEmpty()) {
            Users user = usersRepository.findById(userId).orElseThrow();
            savePostImages(files, postId, user);
        }
        return postId;
    }

    /**
     * [UPDATE] 게시글 내용 수정 및 이미지 교체
     */
    @Transactional
    public void updatePostWithImages(Long postId, GroupBuyPostCreateRequest request, List<MultipartFile> files, Users user) {
        GroupBuyPost post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 존재하지 않습니다."));

        // 1. 글 내용 수정 (더티 체킹 활용을 위해 필드 업데이트 로직 필요)
        // post.update(request); // Entity에 해당 메서드 추가 필요
        
        // 2. 이미지 교체 (기존 이미지 삭제 후 새 이미지 저장)
        if (files != null && !files.isEmpty()) {
            updatePostImages(files, postId, user);
        }
    }

    /**
     * currentParticipants가 가장 높은 게시글 하나 반환 (같으면 최신순)
     */
    public GroupBuyPostResponse getMostPopularPost() {
        GroupBuyPost post = postRepository.findFirstByOrderByCurrentParticipantsDescCreatedAtDesc()
                .orElseThrow(() -> new IllegalArgumentException("게시글이 존재하지 않습니다."));
        return convertToResponse(post);
    }

    /**
     * [READ] Entity -> DTO 변환 시 이미지 URL 리스트 포함
     */
    private GroupBuyPostResponse convertToResponse(GroupBuyPost post) {
        // 이미지 엔티티에서 URL만 뽑아내기
        List<String> imageUrls = post.getImages().stream()
                .map(img -> img.getImageInfo().getImageUrl())
                .collect(Collectors.toList());

        return GroupBuyPostResponse.builder()
                .postId(post.getPostId())
                .title(post.getTitle())
                .description(post.getDescription())
                .priceTotal(post.getPriceTotal())
                .meetPlaceText(post.getMeetPlaceText())
                .status(post.getStatus())
                .categoryName(post.getCategory().getName())
                .authorNickname(post.getUser().getNickname())
                .createdAt(post.getCreatedAt())
                .currentParticipants(post.getCurrentParticipants())
                .maxParticipants(post.getMaxParticipants())
                .lat(post.getLat())
                .lng(post.getLng())
                .imageUrls(imageUrls) // 이 부분을 추가해야 상세/목록에서 사진이 보입니다.
                .build();
    }
}
