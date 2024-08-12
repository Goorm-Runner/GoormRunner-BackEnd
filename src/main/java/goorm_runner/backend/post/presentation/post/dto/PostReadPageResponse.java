package goorm_runner.backend.post.presentation.post.dto;

import goorm_runner.backend.global.PageMetaData;
import goorm_runner.backend.post.application.post.dto.PostReadPageResult;

import java.util.List;

public record PostReadPageResponse(List<PostOverview> overviews, PageMetaData pageMetaData) {
    public static PostReadPageResponse from(PostReadPageResult postReadPageResult) {
        List<PostOverview> postOverviews = postReadPageResult.overviews()
                .stream()
                .map(PostOverview::from)
                .toList();
        return new PostReadPageResponse(postOverviews, postReadPageResult.pageMetaData());
    }
}
