package goorm_runner.backend.domain.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MemberSignupRequest {
    //dto ( 일반적인 로그인을 행할 때 들어가야 할
    @NotBlank(message = "Login ID is required")
    private String loginId;

    @NotBlank(message = "Username is required")
    private String username;

    @NotBlank(message = "Password is required")
    @Size(min = 6, message = "Password must be at least 6 characters")
    private String password;

    @NotBlank(message = "Role is required")
    private String role; // USER or ADMIN

    @NotBlank(message = "Sex is required")
    private String sex; // M or F

    @NotBlank(message = "Birthdate is required")
    private String birth; // Format: yyyy-MM-dd
}
