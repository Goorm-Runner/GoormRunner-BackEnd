package goorm_runner.backend.member.security.application.exception;

import goorm_runner.backend.global.ErrorCode;

public class AuthException extends IllegalArgumentException {

    private final ErrorCode errorCode;

    public AuthException(ErrorCode errorCode) {
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
