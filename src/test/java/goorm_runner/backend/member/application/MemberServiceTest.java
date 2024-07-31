package goorm_runner.backend.member.application;

import goorm_runner.backend.member.domain.Member;
import goorm_runner.backend.member.security.application.AuthService;
import goorm_runner.backend.member.security.dto.MemberSignupRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class MemberServiceTest {

    @Autowired
    private AuthService authService;

    @Autowired
    private MemberService memberService;

    @Test
    void findMemberIdByUsername() {
        //given
        String loginId = "loginId";
        String nickname = "username";
        String password = "password";
        String role = "user";
        String sex = "male";
        String birth = "2024-07-30";
        MemberSignupRequest request = new MemberSignupRequest(loginId, nickname, password, role, sex, birth);

        Member member = authService.signup(request);

        //when
        Long memberId = memberService.findMemberIdByUsername(loginId);

        //then
        assertThat(member.getId()).isEqualTo(memberId);
    }
}