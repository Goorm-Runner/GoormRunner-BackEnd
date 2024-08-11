package goorm_runner.backend.post.presentation.post.dto;

import lombok.Builder;

@Builder
public record PostCreateResponse(String categoryName, Long postId, String title, String content, String createdAt) {
}
