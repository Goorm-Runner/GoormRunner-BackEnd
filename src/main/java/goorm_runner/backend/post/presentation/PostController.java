package goorm_runner.backend.post.presentation;

import goorm_runner.backend.member.application.MemberService;
import goorm_runner.backend.member.security.SecurityMember;
import goorm_runner.backend.post.application.PostReadService;
import goorm_runner.backend.post.application.PostService;
import goorm_runner.backend.post.domain.Post;
import goorm_runner.backend.post.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;
    private final PostReadService postReadService;
    private final MemberService memberService;

    @PostMapping("/categories/{categoryName}/posts")
    public ResponseEntity<PostCreateResponse> createPost(
            @AuthenticationPrincipal SecurityMember securityMember,
            @PathVariable String categoryName,
            @RequestBody PostCreateRequest request) {

        String username = securityMember.getUsername();
        Long authorId = memberService.getMemberIdByUsername(username);

        Post post = postService.create(request, authorId, categoryName);
        PostCreateResponse response = getCreateResponse(post);

        URI location = newUri(response);

        return ResponseEntity.created(location)
                .body(response);
    }

    @GetMapping("/categories/{categoryName}/posts/{postId}")
    public ResponseEntity<PostReadResponse> readPost(@PathVariable String categoryName, @PathVariable Long postId) {

        Post post = postReadService.readPost(postId);
        PostReadResponse response = getReadResponse(categoryName, postId, post);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/categories/{categoryName}/posts")
    public ResponseEntity<PostReadPageResponse> readPage(@PathVariable String categoryName, @RequestParam int pageNumber, @RequestParam int pageSize) {

        Page<Post> posts = postReadService.readPage(categoryName, PageRequest.of(pageNumber, pageSize));

        List<PostOverview> overviews = posts.stream()
                .map(post -> PostOverview.from(post, postService.getAuthorName(post.getId())))
                .toList();

        ResponseMetaData responseMetaData = ResponseMetaData.of(posts);

        PostReadPageResponse response = new PostReadPageResponse(overviews, responseMetaData);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/categories/{categoryName}/posts/{postId}")
    public ResponseEntity<PostUpdateResponse> updatePost(
            @PathVariable String categoryName, @PathVariable Long postId, @RequestBody PostUpdateRequest request) {

        Post post = postService.update(request, postId);
        PostUpdateResponse response = getUpdateResponse(categoryName, postId, post);

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/categories/{ignoredCategoryName}/posts/{postId}")
    public ResponseEntity<Void> deletePost(@PathVariable String ignoredCategoryName, @PathVariable Long postId) {
        postService.delete(postId);
        return ResponseEntity.noContent().build();
    }

    private PostCreateResponse getCreateResponse(Post post) {
        return PostCreateResponse.builder()
                .categoryName(post.getCategory().name())
                .postId(post.getId())
                .title(post.getTitle())
                .content(post.getContent())
                .createdAt(post.getCreatedAt().toString())
                .build();
    }

    private PostReadResponse getReadResponse(String categoryName, Long postId, Post post) {
        return PostReadResponse.builder()
                .categoryName(categoryName)
                .postId(postId)
                .title(post.getTitle())
                .content(post.getContent())
                .likeCount(post.getLikeCount())
                .createdAt(post.getCreatedAt())
                .updatedAt(post.getUpdatedAt())
                .build();
    }

    private PostUpdateResponse getUpdateResponse(String categoryName, Long postId, Post post) {
        return PostUpdateResponse.builder()
                .categoryName(categoryName)
                .postId(postId)
                .title(post.getTitle())
                .content(post.getContent())
                .createdAt(post.getCreatedAt())
                .updatedAt(post.getUpdatedAt())
                .build();
    }

    private URI newUri(PostCreateResponse response) {
        return ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(response.postId())
                .toUri();
    }
}
