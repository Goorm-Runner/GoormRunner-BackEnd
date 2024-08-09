package goorm_runner.backend.mail.application.exception;

import goorm_runner.backend.global.ErrorCode;
import lombok.Getter;

@Getter
public class MailException extends RuntimeException {
    private final ErrorCode errorCode;

    public MailException(ErrorCode errorCode, Throwable cause) {
        super(errorCode.getMessage(), cause);
        this.errorCode = errorCode;
    }

    @Override
    public String getMessage() {
        return errorCode.getMessage();
    }
}
