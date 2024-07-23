package goorm_runner.backend.domain.auth;

import goorm_runner.backend.domain.config.jwt.JwtTokenProvider;
import goorm_runner.backend.domain.dto.ApiResponse;
import goorm_runner.backend.domain.dto.LoginRequest;
import goorm_runner.backend.domain.dto.LoginResponse;
import goorm_runner.backend.domain.dto.MemberSignupRequest;
import goorm_runner.backend.domain.member.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {
    private final MemberService memberService;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;


    @PostMapping("/signup")
    public ResponseEntity<ApiResponse<String>> signup(@Validated @RequestBody MemberSignupRequest request) {
        memberService.signup(request);
        return ResponseEntity.ok(new ApiResponse<>("회원 가입이 완료 되었습니다"));
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Validated @RequestBody LoginRequest request) {
        String token = memberService.login(request);
        return ResponseEntity.ok(new LoginResponse(token));
    }


}
