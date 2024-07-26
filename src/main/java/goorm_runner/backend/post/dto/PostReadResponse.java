package goorm_runner.backend.post.dto;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record PostReadResponse(String categoryName, Long postId, String title, String content, Short likeCount,
                               LocalDateTime createdAt, LocalDateTime updatedAt) {
}
