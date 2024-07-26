package goorm_runner.backend.post.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Builder
@Getter
public class PostCreateResponse {
    private final String categoryName;

    private final Long postId;

    private final String title;

    private final String content;

    private final String createdAt;
}
