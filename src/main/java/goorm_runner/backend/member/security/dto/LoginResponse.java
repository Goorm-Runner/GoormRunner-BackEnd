package goorm_runner.backend.member.security.dto;

import lombok.Getter;

@Getter
public class LoginResponse {
    private String token;
    public LoginResponse(String token) {
        this.token=token;
    }
}
