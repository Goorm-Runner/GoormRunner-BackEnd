package goorm_runner.backend.mail.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VerificationResponse {
    private String message;

    public VerificationResponse(String message) {
        this.message = message;
    }
}
