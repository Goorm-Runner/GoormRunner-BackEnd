package goorm_runner.backend.market.dto;


public record MarketCreateRequest(String title, String content, Integer price, Integer delivery,  String imageUrl) {
}