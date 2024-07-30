package goorm_runner.backend.member.application.exception;

import goorm_runner.backend.global.ErrorCode;

public class MemberException extends IllegalArgumentException {

    private final ErrorCode errorCode;

    public MemberException(ErrorCode errorCode) {
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
