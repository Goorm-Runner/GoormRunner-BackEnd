package goorm_runner.backend.market.presentation;

import goorm_runner.backend.market.application.MarketCommentService;
import goorm_runner.backend.market.domain.MarketComment;
import goorm_runner.backend.market.dto.MarketCommentCreateRequest;
import goorm_runner.backend.market.dto.MarketCommentResponse;
import goorm_runner.backend.market.dto.MarketCommentUpdateRequest;
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
public class MarketCommentController {

    private final MarketCommentService commentService;
    private final MemberService memberService;

    @PostMapping
    public ResponseEntity<MarketCommentResponse> createComment(
            @AuthenticationPrincipal SecurityMember securityMember,
            @PathVariable Long marketId,
            @RequestBody MarketCommentCreateRequest request) {

        String username = securityMember.getUsername();
        Long memberId = memberService.findMemberIdByUsername(username);

        MarketComment comment = commentService.createComment(request, marketId, memberId);
        MarketCommentResponse response = toResponse(comment);

        return ResponseEntity.ok(response);
    }

    @PutMapping("/{commentId}")
    public ResponseEntity<MarketCommentResponse> updateComment(
            @PathVariable Long commentId,
            @RequestBody MarketCommentUpdateRequest request) {

        MarketComment comment = commentService.updateComment(commentId, request);
        MarketCommentResponse response = toResponse(comment);

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<Void> deleteComment(@PathVariable Long commentId) {
        commentService.deleteComment(commentId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<MarketCommentResponse>> getComments(@PathVariable Long marketId) {
        List<MarketComment> comments = commentService.getCommentsByMarketId(marketId);
        List<MarketCommentResponse> response = comments.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }

    private MarketCommentResponse toResponse(MarketComment comment) {
        return MarketCommentResponse.builder()
                .commentId(comment.getId())
                .marketId(comment.getMarket().getId())
                .memberId(comment.getMemberId())
                .content(comment.getContent())
                .createdAt(comment.getCreatedAt().toString())
                .updatedAt(comment.getUpdatedAt().toString())
                .build();
    }
}
