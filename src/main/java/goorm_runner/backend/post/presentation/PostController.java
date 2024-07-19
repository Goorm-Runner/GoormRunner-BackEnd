package goorm_runner.backend.post.presentation;

import goorm_runner.backend.member.MemberDetails;
import goorm_runner.backend.post.application.PostService;
import goorm_runner.backend.post.domain.Post;
import goorm_runner.backend.post.dto.PostCreateRequest;
import goorm_runner.backend.post.dto.PostCreateResponse;
import goorm_runner.backend.post.dto.PostUpdateRequest;
import goorm_runner.backend.post.dto.PostUpdateResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @PostMapping("/categories/{categoryName}/posts")
    public ResponseEntity<PostCreateResponse> createPost(
            @AuthenticationPrincipal MemberDetails memberDetails,
            @PathVariable String categoryName,
            PostCreateRequest request) {

        Long authorId = memberDetails.getId();
        Post post = postService.create(request, authorId, categoryName);
        PostCreateResponse response = getResponse(post);

        URI location = newUri(response);

        return ResponseEntity.created(location)
                .body(response);
    }

    @PutMapping("/categories/{categoryName}/posts/{postId}")
    public ResponseEntity<PostUpdateResponse> updatePost(
            @PathVariable String categoryName, @PathVariable Long postId, @RequestBody PostUpdateRequest request) {

        Post post = postService.update(request, postId);
        PostUpdateResponse response = getResponse(categoryName, postId, post);

        return ResponseEntity.ok(response);
    }

    private PostCreateResponse getResponse(Post post) {
        return PostCreateResponse.builder()
                .categoryName(post.getCategory().name())
                .postId(post.getId())
                .title(post.getTitle())
                .content(post.getContent())
                .createdAt(post.getCreatedAt().toString())
                .build();
    }

    private PostUpdateResponse getResponse(String categoryName, Long postId, Post post) {
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
                .buildAndExpand(response.getPostId())
                .toUri();
    }
}
