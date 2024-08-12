package goorm_runner.backend.post.presentation.comment.dto;

import goorm_runner.backend.post.application.comment.dto.CommentResultOverview;

import java.time.LocalDateTime;

public record CommentOverview(Long postId, Long commentId, String content, LocalDateTime createdAt, LocalDateTime updatedAt, String authorName) {
    public static CommentOverview from(CommentResultOverview resultOverview) {
        return new CommentOverview(
                resultOverview.postId(),
                resultOverview.commentId(),
                resultOverview.content(),
                resultOverview.createdAt(),
                resultOverview.updatedAt(),
                resultOverview.authorName()
        );
    }
}
