package goorm_runner.backend.comment.presentation.dto;

import goorm_runner.backend.comment.domain.Comment;

public record CommentCreateResponse(Long postId, Long commentId, String content, String createdAt) {
    public static CommentCreateResponse from(Comment comment) {
        return new CommentCreateResponse(comment.getPostId(), comment.getId(), comment.getContent(), comment.getCreatedAt().toString());
    }
}
