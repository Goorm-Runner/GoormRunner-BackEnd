package goorm_runner.backend.market.presentation;

import goorm_runner.backend.market.application.CommentService;
import goorm_runner.backend.market.domain.Comment;
import goorm_runner.backend.market.dto.CommentCreateRequest;
import goorm_runner.backend.market.dto.CommentUpdateRequest;
import goorm_runner.backend.market.dto.CommentResponse;
import goorm_runner.backend.member.application.MemberService;
import goorm_runner.backend.member.security.SecurityMember;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/market/{marketId}/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;
    private final MemberService memberService;

    @PostMapping
    public ResponseEntity<CommentResponse> createComment(
            @AuthenticationPrincipal SecurityMember securityMember,
            @PathVariable Long marketId,
            @RequestBody CommentCreateRequest request) {

        String username = securityMember.getUsername();
        Long memberId = memberService.getMemberIdByUsername(username);

        Comment comment = commentService.createComment(request, marketId, memberId);
        CommentResponse response = toResponse(comment);

        return ResponseEntity.ok(response);
    }

    @PutMapping("/{commentId}")
    public ResponseEntity<CommentResponse> updateComment(
            @PathVariable Long commentId,
            @RequestBody CommentUpdateRequest request) {

        Comment comment = commentService.updateComment(commentId, request);
        CommentResponse response = toResponse(comment);

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<Void> deleteComment(@PathVariable Long commentId) {
        commentService.deleteComment(commentId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<CommentResponse>> getComments(@PathVariable Long marketId) {
        List<Comment> comments = commentService.getCommentsByMarketId(marketId);
        List<CommentResponse> response = comments.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }

    private CommentResponse toResponse(Comment comment) {
        return CommentResponse.builder()
                .commentId(comment.getId())
                .marketId(comment.getMarket().getId())
                .memberId(comment.getMemberId())
                .content(comment.getContent())
                .createdAt(comment.getCreatedAt().toString())
                .updatedAt(comment.getUpdatedAt().toString())
                .build();
    }
}
