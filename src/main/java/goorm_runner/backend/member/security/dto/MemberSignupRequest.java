package goorm_runner.backend.member.security.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * @param role    USER or ADMIN
 * @param sex     male or female
 * @param birth   Format: yyyy-MM-dd
 */

public record MemberSignupRequest(@NotBlank(message = "Login ID is required") String loginId,
                                  @NotBlank(message = "Nickname is required") String nickname,
                                  @NotBlank(message = "Password is required") @Size(min = 6, message = "Password must be at least 6 characters") String password,
                                  @NotBlank(message = "Role is required") String role,
                                  @NotBlank(message = "Sex is required") String sex,
                                  @NotBlank(message = "Birthdate is required") String birth) {
}
