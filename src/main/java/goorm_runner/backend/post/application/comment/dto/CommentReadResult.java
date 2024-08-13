package goorm_runner.backend.post.application.comment.dto;

import goorm_runner.backend.post.domain.model.Comment;

public record CommentReadResult(Long commentId, String content, String updatedAt) {
    public static CommentReadResult from(Comment comment) {
        return new CommentReadResult(comment.getId(), comment.getContent(), comment.getUpdatedAt().toString());
    }
}
