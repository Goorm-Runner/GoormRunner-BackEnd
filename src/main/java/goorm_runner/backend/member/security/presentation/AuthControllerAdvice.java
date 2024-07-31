package goorm_runner.backend.member.security.presentation;

import goorm_runner.backend.global.ErrorResult;
import goorm_runner.backend.member.security.application.exception.AuthException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class AuthControllerAdvice {

    @ExceptionHandler(AuthException.class)
    public ResponseEntity<ErrorResult> handleException(AuthException e) {

        return ResponseEntity.badRequest()
                .body(new ErrorResult(e.title(), e.getMessage()));
    }
}
