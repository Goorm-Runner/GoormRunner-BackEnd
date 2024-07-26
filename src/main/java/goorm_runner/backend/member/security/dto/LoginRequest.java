package goorm_runner.backend.member.security.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class LoginRequest {
    @NotBlank(message = "Login ID is required")
    private final String loginId;
    @NotBlank(message = "Password is required")
    private final String password;

}
