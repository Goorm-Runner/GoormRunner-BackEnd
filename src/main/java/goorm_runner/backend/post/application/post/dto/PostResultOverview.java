package goorm_runner.backend.post.application.post.dto;

import goorm_runner.backend.post.domain.model.Post;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record PostResultOverview(String categoryName, Long postId, String title, LocalDateTime createdAt,
                                 LocalDateTime updatedAt, String authorName, int likeCount) {
    public static PostResultOverview from(Post post, String authorName, int likeCount) {
        return PostResultOverview.builder()
                .categoryName(post.getCategory().name())
                .postId(post.getId())
                .title(post.getTitle())
                .createdAt(post.getCreatedAt())
                .updatedAt(post.getUpdatedAt())
                .authorName(authorName)
                .likeCount(likeCount)
                .build();
    }
}
