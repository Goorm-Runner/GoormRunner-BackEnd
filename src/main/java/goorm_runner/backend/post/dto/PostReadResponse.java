package goorm_runner.backend.post.dto;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record PostReadResponse(String categoryName, Long postId, String title, String content, int likeCount,
                               LocalDateTime createdAt, LocalDateTime updatedAt) {
}
