package goorm_runner.backend.post.domain.exception;

import goorm_runner.backend.global.ErrorCode;
import lombok.Getter;

@Getter
public class PostException extends IllegalArgumentException {

    private final ErrorCode errorCode;

    public PostException(ErrorCode errorCode) {
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
