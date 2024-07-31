package goorm_runner.backend.member.security.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class MemberSignupRequest {
    //dto ( 일반적인 로그인을 행할 때 들어가야 할
    @NotBlank(message = "Login ID is required")
    private final String loginId;

    @NotBlank(message = "Nickname is required")
    private final String nickname;

    @NotBlank(message = "Password is required")
    @Size(min = 6, message = "Password must be at least 6 characters")
    private final String password;

    @NotBlank(message = "Role is required")
    private final String role; // USER or ADMIN

    @NotBlank(message = "Sex is required")
    private final String sex; // male or female

    @NotBlank(message = "Birthdate is required")
    private final String birth; // Format: yyyy-MM-dd
}
