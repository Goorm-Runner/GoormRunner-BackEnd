package goorm_runner.backend.post.dto;

import lombok.Builder;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Builder
@RequiredArgsConstructor
public class PostUpdateResponse {
    public final String categoryName;
    public final Long postId;
    public final String title;
    public final String content;
    public final LocalDateTime createdAt;
    public final LocalDateTime updatedAt;
}
