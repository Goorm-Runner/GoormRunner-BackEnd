package goorm_runner.backend.postlike.application.exception;

import goorm_runner.backend.global.ErrorCode;
import lombok.Getter;

@Getter
public class PostLikeException extends RuntimeException {

    private final ErrorCode errorCode;

    public PostLikeException(ErrorCode errorCode) {
        this.errorCode = errorCode;
    }

    @Override
    public String getMessage() {
        return errorCode.getMessage();
    }

    public String title() {
        return errorCode.name();
    }
}
