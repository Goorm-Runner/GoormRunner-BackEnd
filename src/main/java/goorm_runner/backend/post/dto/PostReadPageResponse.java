package goorm_runner.backend.post.dto;

import java.util.List;

public record PostReadPageResponse(List<PostOverview> overviews, ResponseMetaData responseMetaData) {
    @Override
    public List<PostOverview> overviews() {
        return List.copyOf(overviews);
    }
}
