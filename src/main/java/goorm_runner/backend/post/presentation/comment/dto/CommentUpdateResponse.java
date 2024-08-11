package goorm_runner.backend.post.presentation.comment.dto;

import goorm_runner.backend.post.domain.model.Comment;

public record CommentUpdateResponse(Long postId, Long commentId, String content, String updatedAt) {
    public static CommentUpdateResponse from(Comment comment) {
        return new CommentUpdateResponse(comment.getPost().getId(), comment.getId(), comment.getContent(), comment.getUpdatedAt().toString());
    }
}
