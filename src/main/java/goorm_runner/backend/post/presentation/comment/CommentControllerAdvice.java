package goorm_runner.backend.post.presentation.comment;

import goorm_runner.backend.global.ErrorCode;
import goorm_runner.backend.global.ErrorResult;
import goorm_runner.backend.post.application.post.exception.PostException;
import goorm_runner.backend.post.domain.exception.CommentException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class CommentControllerAdvice {

    @ExceptionHandler(CommentException.class)
    public ResponseEntity<ErrorResult> handleException(CommentException e) {

        ErrorCode errorCode = e.getErrorCode();

        return new ResponseEntity<>(new ErrorResult(e.title(), e.getMessage()), errorCode.getHttpStatus());
    }

    @ExceptionHandler(PostException.class)
    public ResponseEntity<ErrorResult> handleException(PostException e) {

        ErrorCode errorCode = e.getErrorCode();

        return new ResponseEntity<>(new ErrorResult(e.title(), e.getMessage()), errorCode.getHttpStatus());
    }
}
