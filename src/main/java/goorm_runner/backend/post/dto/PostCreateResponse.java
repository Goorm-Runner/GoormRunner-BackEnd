package goorm_runner.backend.post.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class PostCreateResponse {
    private final String categoryName;
    private final Long postId;
    private final String title;
    private final String content;
    private final String createdAt;

    @Builder
    public PostCreateResponse(String categoryName, Long postId, String title, String content, String createdAt) {
        this.categoryName = categoryName;
        this.postId = postId;
        this.title = title;
        this.content = content;
        this.createdAt = createdAt;
    }
}
