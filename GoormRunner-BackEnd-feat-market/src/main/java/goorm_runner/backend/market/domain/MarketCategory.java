package goorm_runner.backend.market.domain;

public enum MarketCategory {

    UNIFORM("유니폼"),
    PHOTOCARD("KBO포토카드"),
    TICKET("티켓 양도"),
    SIGNBALL("싸인볼"),
    ETC("기타 굿즈");


    private final String category;

    MarketCategory(String category) {
        this.category = category;
    }

    public String getCategory() {
        return category;
    }
}
