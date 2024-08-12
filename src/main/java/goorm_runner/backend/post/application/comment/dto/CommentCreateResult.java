package goorm_runner.backend.post.application.comment.dto;

import goorm_runner.backend.post.domain.model.Comment;

public record CommentCreateResult(Long postId, Long commentId, String content, String createdAt) {
    public static CommentCreateResult from(Comment comment) {
        return new CommentCreateResult(comment.getPost().getId(), comment.getId(), comment.getContent(), comment.getCreatedAt().toString());
    }
}
