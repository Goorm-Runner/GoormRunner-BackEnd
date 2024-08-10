package goorm_runner.backend.post.presentation.comment.dto;

import goorm_runner.backend.global.PageMetaData;

import java.util.List;

public record CommentPageResponse(List<CommentOverview> overviews, PageMetaData pageMetaData) {
}
