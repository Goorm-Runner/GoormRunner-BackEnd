package goorm_runner.backend.comment.presentation.dto;

import goorm_runner.backend.comment.domain.Comment;

import java.time.LocalDateTime;

public record CommentOverview(Long postId, Long commentId, String content, LocalDateTime createdAt, LocalDateTime updatedAt, String authorName) {
    public static CommentOverview of(Long postId, Comment comment, String authorName) {
        return new CommentOverview(postId, comment.getId(), comment.getContent(), comment.getCreatedAt(), comment.getUpdatedAt(), authorName);
    }
}
