package goorm_runner.backend.post.dto;

import goorm_runner.backend.global.PageMetaData;

import java.util.List;

public record PostReadPageResponse(List<PostOverview> overviews, PageMetaData pageMetaData) {
    @Override
    public List<PostOverview> overviews() {
        return List.copyOf(overviews);
    }
}
