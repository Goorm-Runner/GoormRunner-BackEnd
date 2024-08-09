package goorm_runner.backend.mail.application;

import goorm_runner.backend.mail.dto.VerificationCodeRequest;
import goorm_runner.backend.mail.dto.VerificationRequest;
import goorm_runner.backend.mail.dto.VerificationResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
public class VerificationService {

    private final Map<String, VerificationInfo> verificationStore = new ConcurrentHashMap<>();
    private final EmailService emailService;

    public ResponseEntity<VerificationResponse> sendVerificationCode(VerificationRequest request) {
        int code = emailService.sendMail(request.getEmail());
        LocalDateTime expiryDate = LocalDateTime.now().plusMinutes(5);

        verificationStore.put(request.getEmail(), new VerificationInfo(String.valueOf(code), expiryDate));

        return createResponse("Verification code sent! Please check your email.");
    }

    public ResponseEntity<VerificationResponse> verifyCode(VerificationCodeRequest request) {
        VerificationInfo verificationInfo = verificationStore.get(request.getEmail());

        if (verificationInfo == null || verificationInfo.getExpiryDate().isBefore(LocalDateTime.now())) {
            verificationStore.remove(request.getEmail());
            return createResponse("Invalid or expired code", HttpStatus.BAD_REQUEST);
        }

        if (!verificationInfo.getCode().equals(request.getCode())) {
            return createResponse("Invalid code", HttpStatus.BAD_REQUEST);
        }

        verificationStore.remove(request.getEmail());
        return createResponse("Email verified successfully!");
    }

    private ResponseEntity<VerificationResponse> createResponse(String message) {
        return createResponse(message, HttpStatus.OK);
    }

    private ResponseEntity<VerificationResponse> createResponse(String message, HttpStatus status) {
        return ResponseEntity.status(status)
                .contentType(MediaType.APPLICATION_JSON)
                .body(new VerificationResponse(message));
    }
}