package goorm_runner.backend.post.presentation.post;

import goorm_runner.backend.global.ErrorCode;
import goorm_runner.backend.member.application.MemberService;
import goorm_runner.backend.member.security.SecurityMember;
import goorm_runner.backend.post.application.post.PostReadService;
import goorm_runner.backend.post.application.post.PostService;
import goorm_runner.backend.post.application.post.dto.PostCreateResult;
import goorm_runner.backend.post.application.post.dto.PostReadPageResult;
import goorm_runner.backend.post.application.post.dto.PostUpdateResult;
import goorm_runner.backend.post.domain.PostQueryRepository;
import goorm_runner.backend.post.domain.exception.PostException;
import goorm_runner.backend.post.domain.model.Category;
import goorm_runner.backend.post.domain.model.Post;
import goorm_runner.backend.post.presentation.post.dto.*;
import goorm_runner.backend.postlike.application.PostLikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

import static goorm_runner.backend.global.ErrorCode.INVALID_CATEGORY;

@RestController
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;
    private final PostReadService postReadService;
    private final MemberService memberService;
    private final PostLikeService postLikeService;
    private final PostQueryRepository postQueryRepository;

    @PostMapping("/categories/{categoryName}/posts")
    public ResponseEntity<PostCreateResponse> createPost(
            @AuthenticationPrincipal SecurityMember securityMember,
            @PathVariable String categoryName,
            @RequestBody PostCreateRequest request) {

        String username = securityMember.getUsername();
        Long authorId = memberService.findMemberIdByUsername(username);

        Category category = toCategory(categoryName.toUpperCase());

        PostCreateResult result = postService.create(request.title(), request.content(), authorId, category);
        PostCreateResponse response = PostCreateResponse.from(result);

        URI location = newUri(response);

        return ResponseEntity.created(location)
                .body(response);
    }

    @GetMapping("/categories/{ignoredCategoryName}/posts/{postId}")
    public ResponseEntity<PostReadResponse> readPost(@PathVariable String ignoredCategoryName, @PathVariable Long postId) {

        Post post = postQueryRepository.findById(postId)
                .orElseThrow(() -> new PostException(ErrorCode.POST_NOT_FOUND));

        int likes = postLikeService.countPostLikes(postId);
        PostReadResponse response = PostReadResponse.of(post, likes);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/categories/{categoryName}/posts")
    public ResponseEntity<PostReadPageResponse> readPage(@PathVariable String categoryName, @RequestParam int pageNumber, @RequestParam int pageSize) {

        Category category = toCategory(categoryName.toUpperCase());

        PostReadPageResult result = postReadService.readPage(category, PageRequest.of(pageNumber, pageSize));

        PostReadPageResponse response = PostReadPageResponse.from(result);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/categories/{ignoredCategoryName}/posts/{postId}")
    public ResponseEntity<PostUpdateResponse> updatePost(
            @PathVariable String ignoredCategoryName, @PathVariable Long postId, @RequestBody PostUpdateRequest request,
            @AuthenticationPrincipal SecurityMember securityMember) {

        String username = securityMember.getUsername();
        Long loginId = memberService.findMemberIdByUsername(username);

        PostUpdateResult result = postService.update(request.title(), request.content(), postId, loginId);
        PostUpdateResponse response = PostUpdateResponse.from(result);

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/categories/{ignoredCategoryName}/posts/{postId}")
    public ResponseEntity<Void> deletePost(@PathVariable String ignoredCategoryName, @PathVariable Long postId,
                                           @AuthenticationPrincipal SecurityMember securityMember) {
        String username = securityMember.getUsername();
        Long loginId = memberService.findMemberIdByUsername(username);

        postService.delete(postId, loginId);
        return ResponseEntity.noContent().build();
    }

    private Category toCategory(String category) {
        try {
            return Category.valueOf(category);
        } catch (IllegalArgumentException e) {
            throw new PostException(INVALID_CATEGORY);
        }
    }

    private URI newUri(PostCreateResponse response) {
        return ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(response.postId())
                .toUri();
    }
}
