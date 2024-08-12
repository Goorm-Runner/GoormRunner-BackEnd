package goorm_runner.backend.post.presentation.post.dto;

import goorm_runner.backend.post.application.post.dto.PostResultOverview;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record PostOverview(String categoryName, Long postId, String title, LocalDateTime createdAt,
                           LocalDateTime updatedAt, String authorName, int likeCount) {
    public static PostOverview from(PostResultOverview resultOverview) {
        return new PostOverview(
                resultOverview.categoryName(),
                resultOverview.postId(),
                resultOverview.title(),
                resultOverview.createdAt(),
                resultOverview.updatedAt(),
                resultOverview.authorName(),
                resultOverview.likeCount());
    }
}
