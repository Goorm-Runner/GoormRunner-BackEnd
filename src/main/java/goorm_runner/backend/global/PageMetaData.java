package goorm_runner.backend.global;

import lombok.Builder;
import org.springframework.data.domain.Page;

@Builder
public record PageMetaData(int number, int size, boolean isFirst, boolean isLast, boolean hasNext,
                           boolean hasPrevious) {

    public static PageMetaData of(Page<?> page) {
        return PageMetaData.builder()
                .number(page.getNumber())
                .size(page.getSize())
                .isFirst(page.isFirst())
                .isLast(page.isLast())
                .hasNext(page.hasNext())
                .hasPrevious(page.hasPrevious())
                .build();
    }
}
