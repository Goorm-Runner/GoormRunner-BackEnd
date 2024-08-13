package goorm_runner.backend.post.presentation.comment.dto;

public record CommentCreateResponse(Long postId, Long commentId, String content, String createdAt) {
}
