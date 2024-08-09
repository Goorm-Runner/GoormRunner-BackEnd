package goorm_runner.backend.recruitment.presentation;

import goorm_runner.backend.global.ErrorResult;
import goorm_runner.backend.recruitment.application.exception.RecruitmentException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class RecruitmentControllerAdvice {
    @ExceptionHandler(RecruitmentException.class)
    public ResponseEntity<ErrorResult> handleException(RecruitmentException e) {
        return ResponseEntity.badRequest()
                .body(new ErrorResult(e.title(), e.getMessage()));
    }
}
