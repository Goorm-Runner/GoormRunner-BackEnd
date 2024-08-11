package goorm_runner.backend.post.presentation.post.dto;

import goorm_runner.backend.post.domain.model.Post;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record PostOverview(String categoryName, Long postId, String title, LocalDateTime createdAt,
                           LocalDateTime updatedAt, String authorName, int likeCount) {
    public static PostOverview from(Post post, String authorName, int likeCount) {
        return PostOverview.builder()
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
