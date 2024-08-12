package goorm_runner.backend.mail.presentation;

import goorm_runner.backend.global.ErrorCode;
import goorm_runner.backend.global.ErrorResult;
import goorm_runner.backend.mail.application.exception.MailException;
import goorm_runner.backend.post.application.exception.PostException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class VerificationControllerAdvice {

    @ExceptionHandler(MailException.class)
    public ResponseEntity<ErrorResult> handleException(PostException e) {

        ErrorCode errorCode = e.getErrorCode();

        return new ResponseEntity<>(new ErrorResult(e.title(), e.getMessage()), errorCode.getHttpStatus());
    }
}
