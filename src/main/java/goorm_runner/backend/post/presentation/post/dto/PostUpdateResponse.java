package goorm_runner.backend.post.presentation.post.dto;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record PostUpdateResponse(String categoryName, Long postId, String title, String content,
                                 LocalDateTime createdAt, LocalDateTime updatedAt) {
}
