package goorm_runner.backend.market.exception;

public class MarketNotFoundException extends RuntimeException {
    public MarketNotFoundException(String message) {
        super(message);
    }
}
