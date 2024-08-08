package goorm_runner.backend.recruitment.application.exception;

import goorm_runner.backend.global.ErrorCode;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class RecruitmentException extends IllegalArgumentException{
    private final ErrorCode errorCode;

    @Override
    public String getMessage() {
        return errorCode.getMessage();
    }

    public String title() {
        return errorCode.name();
    }
}
