package goorm_runner.backend.post.application.comment.dto;

import goorm_runner.backend.post.domain.model.Comment;

import java.time.LocalDateTime;

public record CommentResultOverview(Long postId, Long commentId, String content, LocalDateTime createdAt, LocalDateTime updatedAt, String authorName) {
    public static CommentResultOverview of(Long postId, Comment comment, String authorName) {
        return new CommentResultOverview(postId, comment.getId(), comment.getContent(), comment.getCreatedAt(), comment.getUpdatedAt(), authorName);
    }
}
