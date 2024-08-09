package goorm_runner.backend.comment.presentation.dto;

import goorm_runner.backend.comment.domain.Comment;

public record CommentReadResponse(Long commentId, String content, String updatedAt) {
    public static CommentReadResponse from(Comment comment) {
        return new CommentReadResponse(comment.getId(), comment.getContent(), comment.getContent());
    }
}
