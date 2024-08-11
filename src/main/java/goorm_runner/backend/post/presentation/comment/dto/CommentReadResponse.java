package goorm_runner.backend.post.presentation.comment.dto;

import goorm_runner.backend.post.domain.model.Comment;

public record CommentReadResponse(Long commentId, String content, String updatedAt) {
    public static CommentReadResponse from(Comment comment) {
        return new CommentReadResponse(comment.getId(), comment.getContent(), comment.getContent());
    }
}
