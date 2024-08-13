package goorm_runner.backend.post.application.post.dto;

import goorm_runner.backend.global.PageMetaData;

import java.util.List;

public record PostReadPageResult(List<PostResultOverview> overviews, PageMetaData pageMetaData) {
}
