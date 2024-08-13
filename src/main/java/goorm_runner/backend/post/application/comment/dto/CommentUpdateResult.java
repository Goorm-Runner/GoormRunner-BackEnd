package goorm_runner.backend.post.application.comment.dto;

import goorm_runner.backend.post.domain.model.Comment;

public record CommentUpdateResult(Long postId, Long commentId, String content, String updatedAt) {
    public static CommentUpdateResult from(Comment comment) {
        return new CommentUpdateResult(comment.getPost().getId(), comment.getId(), comment.getContent(), comment.getUpdatedAt().toString());
    }
}
