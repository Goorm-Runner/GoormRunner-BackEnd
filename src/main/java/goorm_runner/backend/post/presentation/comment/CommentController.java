package goorm_runner.backend.post.presentation.comment;

import goorm_runner.backend.global.ErrorCode;
import goorm_runner.backend.global.PageMetaData;
import goorm_runner.backend.member.application.MemberService;
import goorm_runner.backend.member.security.SecurityMember;
import goorm_runner.backend.post.application.comment.CommentReadService;
import goorm_runner.backend.post.application.comment.CommentService;
import goorm_runner.backend.post.application.post.PostReadService;
import goorm_runner.backend.post.application.post.PostService;
import goorm_runner.backend.post.application.post.exception.PostException;
import goorm_runner.backend.post.domain.exception.CommentException;
import goorm_runner.backend.post.domain.model.Comment;
import goorm_runner.backend.post.domain.model.Post;
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
import java.util.Objects;

@RestController
@RequiredArgsConstructor
public class CommentController {

    private final MemberService memberService;
    private final CommentService commentService;
    private final CommentReadService commentReadService;
    private final PostService postService;
    private final PostReadService postReadService;

    @PostMapping("/categories/{ignoredCategoryName}/posts/{postId}/comments")
    public ResponseEntity<CommentCreateResponse> postComment(
            @AuthenticationPrincipal SecurityMember securityMember,
            @PathVariable String ignoredCategoryName,
            @PathVariable Long postId,
            @RequestBody CommentCreateRequest request) {

        String username = securityMember.getUsername();
        Long authorId = memberService.findMemberIdByUsername(username);

        Post post = postReadService.readPost(postId);
        Comment comment = commentService.create(authorId, post, request.content());
        CommentCreateResponse response = CommentCreateResponse.from(comment);

        URI location = newUri(response);

        return ResponseEntity.created(location)
                .body(response);
    }

    @GetMapping("/categories/{ignoredCategoryName}/posts/{postId}/comments/{commentId}")
    public ResponseEntity<CommentReadResponse> getComment(
            @PathVariable String ignoredCategoryName, @PathVariable Long postId, @PathVariable Long commentId) {

        validatePostExisting(postId);

        Comment comment = commentReadService.read(commentId);
        CommentReadResponse response = CommentReadResponse.from(comment);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/categories/{ignoredCategoryName}/posts/{postId}/comments")
    public ResponseEntity<CommentPageResponse> getComments(
            @PathVariable String ignoredCategoryName, @PathVariable Long postId, @RequestParam int pageNumber, @RequestParam int pageSize) {

        validatePostExisting(postId);

        Page<Comment> comments = commentReadService.readPage(postId, PageRequest.of(pageNumber, pageSize));

        List<CommentOverview> commentOverviews = comments.stream()
                .map(comment -> CommentOverview.of(postId, comment, postService.getAuthorName(postId)))
                .toList();

        PageMetaData pageMetaData = PageMetaData.of(comments);

        CommentPageResponse response = new CommentPageResponse(commentOverviews, pageMetaData);

        return ResponseEntity.ok(response);
    }

    @PutMapping("/categories/{ignoredCategoryName}/posts/{postId}/comments/{commentId}")
    public ResponseEntity<CommentUpdateResponse> editComment(
            @AuthenticationPrincipal SecurityMember securityMember, @PathVariable String ignoredCategoryName,
            @PathVariable Long postId, @PathVariable Long commentId, @RequestBody CommentUpdateRequest request) {

        String username = securityMember.getUsername();
        Long authorId = memberService.findMemberIdByUsername(username);

        Post post = postReadService.readPost(postId);

        checkAuthor(post, authorId);

        Comment comment = commentService.update(post, commentId, request.content());
        CommentUpdateResponse response = CommentUpdateResponse.from(comment);

        return ResponseEntity.ok()
                .body(response);
    }

    @DeleteMapping("/categories/{ignoredCategoryName}/posts/{postId}/comments/{commentId}")
    public ResponseEntity<Void> deleteComment(
            @AuthenticationPrincipal SecurityMember securityMember, @PathVariable String ignoredCategoryName,
            @PathVariable Long postId, @PathVariable Long commentId) {

        String username = securityMember.getUsername();
        Long authorId = memberService.findMemberIdByUsername(username);

        Post post = postReadService.readPost(postId);

        checkAuthor(post, authorId);

        commentService.delete(post, commentId);
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
        if (!postReadService.existsPost(postId)) {
            throw new CommentException(ErrorCode.POST_NOT_FOUND);
        }
    }

    private void checkAuthor(Post post, Long authorId) {
        if (!Objects.equals(post.getAuthorId(), authorId)) {
            throw new PostException(ErrorCode.ACCESS_DENIED);
        }
    }
}
