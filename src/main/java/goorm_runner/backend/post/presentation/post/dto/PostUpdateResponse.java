package goorm_runner.backend.post.presentation.post.dto;

import goorm_runner.backend.post.application.post.dto.PostUpdateResult;

import java.time.LocalDateTime;

public record PostUpdateResponse(String categoryName, Long postId, String title, String content,
                                 LocalDateTime createdAt, LocalDateTime updatedAt) {
    public static PostUpdateResponse from(PostUpdateResult result) {
        return new PostUpdateResponse(result.categoryName(), result.postId(), result.title(), result.content(), result.createdAt(), result.updatedAt());
    }
}
