package goorm_runner.backend.comment.presentation;

import goorm_runner.backend.comment.application.CommentReadService;
import goorm_runner.backend.comment.application.CommentService;
import goorm_runner.backend.comment.domain.Comment;
import goorm_runner.backend.comment.presentation.dto.CommentCreateRequest;
import goorm_runner.backend.comment.presentation.dto.CommentCreateResponse;
import goorm_runner.backend.comment.presentation.dto.CommentReadResponse;
import goorm_runner.backend.member.application.MemberService;
import goorm_runner.backend.member.security.SecurityMember;
import lombok.RequiredArgsConstructor;
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

    @PostMapping("/categories/{ignoredCategoryName}/posts/{postId}/comments")
    public ResponseEntity<CommentCreateResponse> postComment(
            @AuthenticationPrincipal SecurityMember securityMember,
            @PathVariable String ignoredCategoryName,
            @PathVariable Long postId,
            @RequestBody CommentCreateRequest request) {

        String username = securityMember.getUsername();
        Long authorId = memberService.findMemberIdByUsername(username);

        Comment comment = commentService.create(authorId, postId, request.content());
        CommentCreateResponse response = CommentCreateResponse.from(comment);

        URI location = newUri(response);

        return ResponseEntity.created(location)
                .body(response);
    }

    @GetMapping("/categories/{ignoredCategoryName}/posts/{postId}/comments/{commentId}")
    public ResponseEntity<CommentReadResponse> getComment(
            @PathVariable String ignoredCategoryName,
            @PathVariable Long postId,
            @PathVariable Long commentId) {

        Comment comment = commentReadService.read(commentId);
        CommentReadResponse response = CommentReadResponse.from(comment);

        return ResponseEntity.ok(response);
    }

    private URI newUri(CommentCreateResponse response) {
        return ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(response.postId())
                .toUri();
    }
}
