package goorm_runner.backend.post.dto;

import lombok.Builder;

@Builder
public record PostCreateResponse(String categoryName, Long postId, String title, String content, String createdAt) {
}
