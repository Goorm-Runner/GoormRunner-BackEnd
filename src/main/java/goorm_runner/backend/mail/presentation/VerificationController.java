package goorm_runner.backend.mail.presentation;

import goorm_runner.backend.mail.application.VerificationService;
import goorm_runner.backend.mail.dto.VerificationCodeRequest;
import goorm_runner.backend.mail.dto.VerificationRequest;
import goorm_runner.backend.mail.dto.VerificationResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class VerificationController {
    private final VerificationService verificationService;

    @PostMapping("/sendCode")
    public ResponseEntity<VerificationResponse> sendVerificationCode(@RequestBody VerificationRequest request) {
        return verificationService.sendVerificationCode(request);
    }

    @PostMapping("/verifyCode")
    public ResponseEntity<VerificationResponse> verifyCode(@RequestBody VerificationCodeRequest request) {
        return verificationService.verifyCode(request);
    }
}
