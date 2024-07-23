package goorm_runner.backend.post.dto;

import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class PostReadPageResponse {
    private final List<PostOverview> overviews;
    private final ResponseMetaData responseMetaData;
}
