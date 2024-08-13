package goorm_runner.backend.post.application.post.dto;

import goorm_runner.backend.post.domain.model.Post;

public record PostCreateResult(String categoryName, Long postId, String title, String content, String createdAt) {
    public static PostCreateResult from(Post post) {
        return new PostCreateResult(post.getCategory().name(), post.getId(), post.getTitle(), post.getContent(), post.getCreatedAt().toString());
    }
}
