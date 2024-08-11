package goorm_runner.backend.market.dto;

import goorm_runner.backend.market.domain.MarketStatus;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record MarketReadResponse(String categoryName, Long marketId, String title, String content,
                                 Integer price, int likeCount, MarketStatus status,
                                 LocalDateTime createdAt, LocalDateTime updatedAt) {

}