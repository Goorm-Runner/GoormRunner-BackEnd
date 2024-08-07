package goorm_runner.backend.market.dto;

import goorm_runner.backend.market.domain.Market;
import goorm_runner.backend.market.domain.MarketStatus;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MarketOverview {
    private Long marketId;
    private String title;
    private String categoryName;
    private Integer price;
    private Integer likeCount;
    private MarketStatus status;
    public static MarketOverview from(Market market) {
        return MarketOverview.builder()
                .marketId(market.getId())
                .title(market.getTitle())
                .categoryName(market.getCategory().name())
                .price(market.getPrice())
                .likeCount(market.getLikeCount())
                .status(market.getStatus())
                .build();
    }
}