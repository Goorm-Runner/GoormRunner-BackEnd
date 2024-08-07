package goorm_runner.backend.member.security.presentation;

import goorm_runner.backend.member.security.application.AuthService;
import goorm_runner.backend.member.security.config.jwt.JwtTokenProvider;
import goorm_runner.backend.member.security.dto.ApiResponse;
import goorm_runner.backend.member.security.dto.LoginRequest;
import goorm_runner.backend.member.security.dto.LoginResponse;
import goorm_runner.backend.member.security.dto.MemberSignupRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService authService;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/signup")
    public ResponseEntity<ApiResponse<String>> signup(@Validated @RequestBody MemberSignupRequest request) {
        authService.signup(request);
        return ResponseEntity.ok(new ApiResponse<>("회원 가입이 완료 되었습니다"));
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Validated @RequestBody LoginRequest request) {
        String token = authService.login(request);
        return ResponseEntity.ok(new LoginResponse(token));
    }

    @GetMapping("/login/google")
    public ResponseEntity<?> googleLogin() {
        return ResponseEntity.ok("Google 로그인 페이지로 리다이렉트됩니다.");
    }

    @GetMapping("/oauth2/redirect")
    public ResponseEntity<?> oauth2Redirect(@RequestParam String token) {
        return ResponseEntity.ok(Collections.singletonMap("token", token));
    }



}
