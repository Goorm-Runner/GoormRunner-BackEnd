package goorm_runner.backend.post.presentation.comment.dto;

import goorm_runner.backend.post.application.comment.dto.CommentUpdateResult;

public record CommentUpdateResponse(Long postId, Long commentId, String content, String updatedAt) {
    public static CommentUpdateResponse from(CommentUpdateResult result) {
        return new CommentUpdateResponse(result.postId(), result.commentId(), result.content(), result.updatedAt());
    }
}
