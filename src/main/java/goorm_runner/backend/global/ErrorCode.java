package goorm_runner.backend.global;

import lombok.Getter;
import org.springframework.http.HttpStatus;

public enum ErrorCode {
    MEMBER_NOT_FOUND(4001, HttpStatus.BAD_REQUEST, "존재하지 않는 유저입니다."),

    POST_NOT_FOUND(4001, HttpStatus.BAD_REQUEST, "게시글을 찾을 수 없습니다."),
    INVALID_CATEGORY(4002, HttpStatus.BAD_REQUEST, "유효하지 않은 카테고리입니다."),
    EMPTY_TITLE(4003, HttpStatus.BAD_REQUEST, "제목을 입력해주세요."),
    EMPTY_CONTENT(4004, HttpStatus.BAD_REQUEST, "본문 내용을 입력해주세요.");

    private final int code;
    private final HttpStatus httpStatus;
    @Getter
    private final String message;

    ErrorCode(int code, HttpStatus httpStatus, String message) {
        this.code = code;
        this.httpStatus = httpStatus;
        this.message = message;
    }

}
