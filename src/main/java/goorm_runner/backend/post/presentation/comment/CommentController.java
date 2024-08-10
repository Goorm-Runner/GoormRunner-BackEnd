package goorm_runner.backend.post.presentation.comment;

import goorm_runner.backend.global.PageMetaData;
import goorm_runner.backend.member.application.MemberService;
import goorm_runner.backend.member.security.SecurityMember;
import goorm_runner.backend.post.application.comment.CommentReadService;
import goorm_runner.backend.post.application.comment.CommentService;
import goorm_runner.backend.post.application.post.PostService;
import goorm_runner.backend.post.domain.comment.Comment;
import goorm_runner.backend.post.presentation.comment.dto.*;
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
public class CommentController {

    private final MemberService memberService;
    private final CommentService commentService;
    private final CommentReadService commentReadService;
    private final PostService postService;

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
            @PathVariable String ignoredCategoryName, @PathVariable Long postId, @PathVariable Long commentId) {

        Comment comment = commentReadService.read(postId, commentId);
        CommentReadResponse response = CommentReadResponse.from(comment);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/categories/{ignoredCategoryName}/posts/{postId}/comments")
    public ResponseEntity<CommentPageResponse> getComments(
            @PathVariable String ignoredCategoryName, @PathVariable Long postId, @RequestParam int pageNumber, @RequestParam int pageSize) {

        Page<Comment> comments = commentReadService.readPage(postId, PageRequest.of(pageNumber, pageSize));

        List<CommentOverview> commentOverviews = comments.stream()
                .map(comment -> CommentOverview.of(postId, comment, postService.getAuthorName(postId)))
                .toList();

        PageMetaData pageMetaData = PageMetaData.of(comments);

        CommentPageResponse response = new CommentPageResponse(commentOverviews, pageMetaData);

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
