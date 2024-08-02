package goorm_runner.backend.post.presentation;

import goorm_runner.backend.global.ErrorResult;
import goorm_runner.backend.post.application.exception.PostException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class PostControllerAdvice {

    @ExceptionHandler(PostException.class)
    public ResponseEntity<ErrorResult> handleException(PostException e) {

        return ResponseEntity.badRequest()
                .body(new ErrorResult(e.title(), e.getMessage()));
    }
}
