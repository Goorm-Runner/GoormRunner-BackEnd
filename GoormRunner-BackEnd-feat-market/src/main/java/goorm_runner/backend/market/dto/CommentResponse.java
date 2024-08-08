package goorm_runner.backend.market.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CommentResponse {
    private Long commentId;
    private Long marketId;
    private Long memberId;
    private String content;
    private String createdAt;
    private String updatedAt;
}
