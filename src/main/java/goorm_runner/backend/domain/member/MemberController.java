package goorm_runner.backend.domain.member;

import goorm_runner.backend.domain.dto.MemberSignupRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/members")
//회원가입 Controller 구성
public class MemberController {
    private final MemberService memberService;


    @PostMapping("/signup")
    public ResponseEntity<String> signup(@RequestBody @Validated MemberSignupRequest request)
    {
        memberService.signup(request);
        return ResponseEntity.status(HttpStatus.CREATED).body("User registered success");
    }


}
