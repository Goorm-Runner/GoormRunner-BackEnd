package goorm_runner.backend.mail.dto;

import lombok.Getter;

@Getter
public class VerificationCodeRequest {
    private String code;
    private String email;
}
