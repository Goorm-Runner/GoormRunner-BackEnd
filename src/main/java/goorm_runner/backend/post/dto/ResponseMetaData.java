package goorm_runner.backend.post.dto;

import lombok.Builder;
import org.springframework.data.domain.Page;

@Builder
public record ResponseMetaData(int number, int size, boolean isFirst, boolean isLast, boolean hasNext,
                               boolean hasPrevious) {

    public static ResponseMetaData of(Page<?> page) {
        return ResponseMetaData.builder()
                .number(page.getNumber())
                .size(page.getSize())
                .isFirst(page.isFirst())
                .isLast(page.isLast())
                .hasNext(page.hasNext())
                .hasPrevious(page.hasPrevious())
                .build();
    }
}
