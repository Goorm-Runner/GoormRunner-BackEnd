package goorm_runner.backend.market.domain;

public enum MarketStatus {

    NEW("새 상품"),
    USED_GOOD("중고 상품 - 상"),
    USED_FAIR("중고 상품 - 중");


    private final String displayName;

    MarketStatus(String displayName) {
        this.displayName = displayName;
    }

    @Override
    public String toString() {
        return displayName;
    }
}