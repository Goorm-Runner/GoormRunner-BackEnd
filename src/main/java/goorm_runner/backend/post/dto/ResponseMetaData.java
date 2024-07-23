package goorm_runner.backend.post.dto;

import org.springframework.data.domain.Page;

public class ResponseMetaData {

    private final int number;

    private final int size;

    private final boolean isFirst;

    private final boolean isLast;

    private final boolean hasNext;

    private final boolean hasPrevious;

    private ResponseMetaData(Page<?> page) {
        number = page.getNumber();
        size = page.getSize();
        isFirst = page.isFirst();
        isLast = page.isLast();
        hasNext = page.hasNext();
        hasPrevious = page.hasPrevious();
    }

    public static ResponseMetaData of(Page<?> page) {
        return new ResponseMetaData(page);
    }
}
