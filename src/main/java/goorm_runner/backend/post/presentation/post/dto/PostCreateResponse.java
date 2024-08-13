package goorm_runner.backend.post.presentation.post.dto;

import goorm_runner.backend.post.application.post.dto.PostCreateResult;

public record PostCreateResponse(String categoryName, Long postId, String title, String content, String createdAt) {
    public static PostCreateResponse from(PostCreateResult result) {
        return new PostCreateResponse(result.categoryName(), result.postId(), result.title(), result.content(), result.createdAt());
    }
}
