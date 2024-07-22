package goorm_runner.backend.post.dto;

import lombok.Builder;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Builder
@RequiredArgsConstructor
public class PostReadResponse {
    private final String categoryName;
    private final Long postId;
    private final String title;
    private final String content;
    private final Short likeCount;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;
}
