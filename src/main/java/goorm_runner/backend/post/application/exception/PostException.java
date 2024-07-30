package goorm_runner.backend.post.application.exception;

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
