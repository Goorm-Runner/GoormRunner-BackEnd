package goorm_runner.backend.market.domain;

public enum MarketStatus {

    NEW("새 상품"),
    USED_GOOD("중고 상품 - 상"),
    USED_FAIR("중고 상품 - 중");


    private final String status;

    MarketStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }
}