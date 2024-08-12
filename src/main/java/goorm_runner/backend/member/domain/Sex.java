package goorm_runner.backend.member.domain;

import java.util.Arrays;

/**
 * <a href="https://en.wikipedia.org/wiki/ISO/IEC_5218">...</a>
 */

public enum Sex {
    NOT_KNOWN('0'),
    MALE('1'),
    FEMALE('2'),
    NOT_APPLICABLE('9');

    private final char code;

    Sex(char code) {
        this.code = code;
    }

    public char getCode() {
        return code;
    }

    public static Sex fromCode(char code) {
        return Arrays.stream(Sex.values())
                .filter(sex -> sex.getCode() == code)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("wrong sex code"));
    }
}
