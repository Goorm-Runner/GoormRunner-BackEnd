package goorm_runner.backend.market.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MarketCreateResponse {
    private String categoryName;
    private Long marketId;
    private String title;
    private String content;
    private String createdAt;
}