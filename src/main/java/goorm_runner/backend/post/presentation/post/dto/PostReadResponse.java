package goorm_runner.backend.post.presentation.post.dto;

import goorm_runner.backend.post.domain.model.Post;

import java.time.LocalDateTime;

public record PostReadResponse(String categoryName, Long postId, String title, String content, int likeCount,
                               LocalDateTime createdAt, LocalDateTime updatedAt) {
    public static PostReadResponse of(Post post, int likes) {
        return new PostReadResponse(post.getCategory().name(), post.getId(), post.getTitle(), post.getContent(), likes, post.getCreatedAt(), post.getUpdatedAt());
    }
}
