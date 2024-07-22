package goorm_runner.backend.post.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Builder
public class PostCreateResponse {
    private final String categoryName;

    @Getter
    private final Long postId;

    private final String title;

    private final String content;

    private final String createdAt;
}
