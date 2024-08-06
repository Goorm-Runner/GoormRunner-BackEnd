package goorm_runner.backend.postlike.presentation;

import goorm_runner.backend.global.ErrorCode;
import goorm_runner.backend.global.ErrorResult;
import goorm_runner.backend.postlike.application.exception.PostLikeException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class PostLikeControllerAdvice {

    @ExceptionHandler(PostLikeException.class)
    public ResponseEntity<ErrorResult> handleException(PostLikeException e) {

        ErrorCode errorCode = e.getErrorCode();

        return new ResponseEntity<>(new ErrorResult(e.title(), e.getMessage()), errorCode.getHttpStatus());
    }

}
