package goorm_runner.backend.mail.application;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class VerificationInfo {
    private String code;
    private LocalDateTime expiryDate;

    public VerificationInfo(String code, LocalDateTime expiryDate) {
        this.code = code;
        this.expiryDate = expiryDate;
    }
}
