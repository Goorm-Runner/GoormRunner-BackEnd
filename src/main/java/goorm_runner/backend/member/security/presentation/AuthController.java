package goorm_runner.backend.member.security.presentation;

import goorm_runner.backend.member.application.MemberService;
import goorm_runner.backend.member.application.MemberUpdateRequest;
import goorm_runner.backend.member.security.SecurityMember;
import goorm_runner.backend.member.security.application.AuthService;
import goorm_runner.backend.member.security.config.jwt.JwtTokenProvider;
import goorm_runner.backend.member.security.dto.*;
import goorm_runner.backend.recruitment.dto.RecruitmentResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService authService;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;
    private final MemberService memberService;

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

    @DeleteMapping("/drop")
    public ResponseEntity<ApiResponse<String>> memberDrop(@AuthenticationPrincipal SecurityMember securityMember){

        String username = securityMember.getUsername();
        Long memberId = memberService.findMemberIdByUsername(username);

        memberService.deleteMemberById(memberId);
        return ResponseEntity.ok(new ApiResponse<>("회원 탈퇴 완료","204"));
    }

    @PutMapping("/edit")
    public ResponseEntity<ApiResponse<String>> memberEdit(
            @AuthenticationPrincipal SecurityMember securityMember,
            @RequestBody MemberUpdateRequest request){

        String username = securityMember.getUsername();
        Long memberId = memberService.findMemberIdByUsername(username);

        memberService.updateMemberById(memberId, request);
        return ResponseEntity.ok(new ApiResponse<>("회원 정보 수정 완료","201"));
    }

    @GetMapping("/info")
    public ResponseEntity<ApiResponse<MemberRecruitmentResponse>> memberGet(
            @AuthenticationPrincipal SecurityMember securityMember){
        String username = securityMember.getUsername();
        Long memberId = memberService.findMemberIdByUsername(username);

        MemberResponse memberResponse = memberService.getMemberInfo(memberId);
        List<RecruitmentResponse> recruitmentsResponses = memberService.findRecruitmentsByMember(memberId);

        MemberRecruitmentResponse response = new MemberRecruitmentResponse(memberResponse, recruitmentsResponses);
        return ResponseEntity.ok(new ApiResponse<>(response, "200"));
    }
}
