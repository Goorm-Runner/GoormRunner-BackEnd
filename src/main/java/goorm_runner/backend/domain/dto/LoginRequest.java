package goorm_runner.backend.domain.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginRequest {
    @NotBlank(message = "Login ID is required")
    private String loginId;
    @NotBlank(message = "Password is required")
    private String password;

}
