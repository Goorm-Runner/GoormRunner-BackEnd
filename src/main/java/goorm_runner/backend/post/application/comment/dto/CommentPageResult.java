package goorm_runner.backend.post.application.comment.dto;

import goorm_runner.backend.global.PageMetaData;

import java.util.List;

public record CommentPageResult(List<CommentResultOverview> overviews, PageMetaData pageMetaData) {
}
