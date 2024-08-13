package goorm_runner.backend.post.presentation.comment.dto;

import goorm_runner.backend.post.application.comment.dto.CommentReadResult;

public record CommentReadResponse(Long commentId, String content, String updatedAt) {
    public static CommentReadResponse from(CommentReadResult result) {
        return new CommentReadResponse(result.commentId(), result.content(), result.updatedAt());
    }
}
