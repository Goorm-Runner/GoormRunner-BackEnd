package goorm_runner.backend.market.dto;

import lombok.Builder;
import org.springframework.data.domain.Page;

public record MarketResponseMetaData(int number, int size, boolean isFirst, boolean isLast, boolean hasNext, boolean hasPrevious) {

    public static MarketResponseMetaData of(Page<?> page) {
        return new MarketResponseMetaData(
                page.getNumber(),
                page.getSize(),
                page.isFirst(),
                page.isLast(),
                page.hasNext(),
                page.hasPrevious()
        );
    }
}