package goorm_runner.backend.market.dto;


public record MarketUpdateRequest(String title, String content, Integer price, Integer delivery, String imageUrl) {

}