package goorm_runner.backend.post.presentation.comment.dto;

import goorm_runner.backend.global.PageMetaData;
import goorm_runner.backend.post.application.comment.dto.CommentPageResult;

import java.util.List;

public record CommentPageResponse(List<CommentOverview> overviews, PageMetaData pageMetaData) {
    public static CommentPageResponse from(CommentPageResult result) {
        List<CommentOverview> commentOverviews = result.overviews()
                .stream()
                .map(CommentOverview::from)
                .toList();

        return new CommentPageResponse(commentOverviews, result.pageMetaData());
    }
}
