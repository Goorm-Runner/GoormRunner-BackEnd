package goorm_runner.backend.market.dto;

import goorm_runner.backend.global.PageMetaData;

import java.util.List;

public record MarketReadPageResponse(List<MarketOverview> overviews, PageMetaData metaData) {
}