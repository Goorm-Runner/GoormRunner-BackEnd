package goorm_runner.backend.post.presentation.comment;

import goorm_runner.backend.global.ErrorCode;
import goorm_runner.backend.member.application.MemberService;
import goorm_runner.backend.member.security.SecurityMember;
import goorm_runner.backend.post.application.comment.CommentReadService;
import goorm_runner.backend.post.application.comment.CommentService;
import goorm_runner.backend.post.application.comment.dto.CommentCreateResult;
import goorm_runner.backend.post.application.comment.dto.CommentPageResult;
import goorm_runner.backend.post.application.comment.dto.CommentReadResult;
import goorm_runner.backend.post.application.comment.dto.CommentUpdateResult;
import goorm_runner.backend.post.domain.PostQueryRepository;
import goorm_runner.backend.post.domain.exception.CommentException;
import goorm_runner.backend.post.domain.exception.PostException;
import goorm_runner.backend.post.domain.model.Post;
import goorm_runner.backend.post.presentation.comment.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequiredArgsConstructor
public class CommentController {

    private final MemberService memberService;
    private final CommentService commentService;
    private final CommentReadService commentReadService;
    private final PostQueryRepository postQueryRepository;

    @PostMapping("/categories/{ignoredCategoryName}/posts/{postId}/comments")
    public ResponseEntity<CommentCreateResponse> postComment(
            @AuthenticationPrincipal SecurityMember securityMember,
            @PathVariable String ignoredCategoryName,
            @PathVariable Long postId,
            @RequestBody CommentCreateRequest request) {

        String username = securityMember.getUsername();
        Long authorId = memberService.findMemberIdByUsername(username);

        CommentCreateResult result = commentService.create(authorId, postId, request.content());
        CommentCreateResponse response = new CommentCreateResponse(result.postId(), result.commentId(), result.content(), result.createdAt());

        URI location = newUri(response);

        return ResponseEntity.created(location)
                .body(response);
    }

    @GetMapping("/categories/{ignoredCategoryName}/posts/{postId}/comments/{commentId}")
    public ResponseEntity<CommentReadResponse> getComment(
            @PathVariable String ignoredCategoryName, @PathVariable Long postId, @PathVariable Long commentId) {

        validatePostExisting(postId);

        CommentReadResult result = commentReadService.read(commentId);
        CommentReadResponse response = CommentReadResponse.from(result);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/categories/{ignoredCategoryName}/posts/{postId}/comments")
    public ResponseEntity<CommentPageResponse> getComments(
            @PathVariable String ignoredCategoryName, @PathVariable Long postId, @RequestParam int pageNumber, @RequestParam int pageSize) {

        validatePostExisting(postId);

        CommentPageResult commentPageResult = commentReadService.readPage(postId, PageRequest.of(pageNumber, pageSize));

        CommentPageResponse response = CommentPageResponse.from(commentPageResult);

        return ResponseEntity.ok(response);
    }

    @PutMapping("/categories/{ignoredCategoryName}/posts/{postId}/comments/{commentId}")
    public ResponseEntity<CommentUpdateResponse> editComment(
            @AuthenticationPrincipal SecurityMember securityMember, @PathVariable String ignoredCategoryName,
            @PathVariable Long postId, @PathVariable Long commentId, @RequestBody CommentUpdateRequest request) {

        String username = securityMember.getUsername();
        Long loginId = memberService.findMemberIdByUsername(username);

        Post post = postQueryRepository.findById(postId)
                .orElseThrow(() -> new PostException(ErrorCode.POST_NOT_FOUND));

        CommentUpdateResult result = commentService.update(post, commentId, request.content(), loginId);
        CommentUpdateResponse response = CommentUpdateResponse.from(result);

        return ResponseEntity.ok()
                .body(response);
    }

    @DeleteMapping("/categories/{ignoredCategoryName}/posts/{postId}/comments/{commentId}")
    public ResponseEntity<Void> deleteComment(
            @AuthenticationPrincipal SecurityMember securityMember, @PathVariable String ignoredCategoryName,
            @PathVariable Long postId, @PathVariable Long commentId) {

        String username = securityMember.getUsername();
        Long loginId = memberService.findMemberIdByUsername(username);

        Post post = postQueryRepository.findById(postId)
                .orElseThrow(() -> new PostException(ErrorCode.POST_NOT_FOUND));

        commentService.delete(post, commentId, loginId);
        return ResponseEntity.noContent().build();
    }

    private URI newUri(CommentCreateResponse response) {
        return ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(response.postId())
                .toUri();
    }

    private void validatePostExisting(Long postId) {
        if (!postQueryRepository.existsById(postId)) {
            throw new CommentException(ErrorCode.POST_NOT_FOUND);
        }
    }
}
