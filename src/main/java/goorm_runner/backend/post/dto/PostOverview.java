package goorm_runner.backend.post.dto;

import goorm_runner.backend.post.domain.Post;
import lombok.Builder;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Builder
@RequiredArgsConstructor
public class PostOverview {
    private final String categoryName;
    private final Long postId;
    private final String title;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;
    private final String authorName;

    public static PostOverview from(Post post, String authorName) {
        return PostOverview.builder()
                .categoryName(post.getCategory().name())
                .postId(post.getId())
                .title(post.getTitle())
                .createdAt(post.getCreatedAt())
                .updatedAt(post.getUpdatedAt())
                .authorName(authorName)
                .build();
    }
}
