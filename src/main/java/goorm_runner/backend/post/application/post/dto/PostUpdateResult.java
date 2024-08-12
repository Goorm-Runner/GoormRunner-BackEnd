package goorm_runner.backend.post.application.post.dto;

import goorm_runner.backend.post.domain.model.Post;

import java.time.LocalDateTime;

public record PostUpdateResult(String categoryName, Long postId, String title, String content,
                               LocalDateTime createdAt, LocalDateTime updatedAt) {
    public static PostUpdateResult from(Post post) {
        return new PostUpdateResult(post.getCategory().name(), post.getId(), post.getTitle(), post.getContent(), post.getCreatedAt(), post.getUpdatedAt());
    }
}
