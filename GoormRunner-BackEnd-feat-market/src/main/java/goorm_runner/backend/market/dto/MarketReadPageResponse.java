package goorm_runner.backend.market.dto;

import goorm_runner.backend.post.dto.ResponseMetaData;


import java.util.List;

public record MarketReadPageResponse(List<MarketOverview> overviews, ResponseMetaData responseMetaData) {
    @Override
    public List<MarketOverview> overviews() {
        return List.copyOf(overviews);
    }

}