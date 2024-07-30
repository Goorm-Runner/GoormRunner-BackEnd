package goorm_runner.backend.member.security.application;

import goorm_runner.backend.member.application.AuthorityRepository;
import goorm_runner.backend.member.domain.Authority;
import goorm_runner.backend.member.domain.Member;
import goorm_runner.backend.member.domain.Role;
import goorm_runner.backend.member.domain.Sex;
import goorm_runner.backend.member.security.dto.LoginRequest;
import goorm_runner.backend.member.security.dto.MemberSignupRequest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

@SpringBootTest
@Transactional
class AuthServiceTest {

    @Autowired
    private AuthService authService;

    @Autowired
    private AuthorityRepository authorityRepository;

    @BeforeEach
    void setUp() {
        authorityRepository.save(new Authority(1L, "read"));
    }

    @AfterEach
    void tearDown() {
        authorityRepository.deleteAll();
    }

    @Test
    void signup_success() {
        //given
        String id = "id";
        String username = "username";
        String password = "password";
        String role = "user";
        String sex = "male";
        String birth = "2024-07-30";
        MemberSignupRequest request = new MemberSignupRequest(id, username, password, role, sex, birth);

        //when
        Member member = authService.signup(request);

        //then
        assertAll(
                () -> assertThat(member.getLoginId()).isEqualTo(id),
                () -> assertThat(member.getUsername()).isEqualTo(username),
                () -> assertThat(member.getPassword()).isNotBlank(), //password와 다름(암호화)
                () -> assertThat(member.getRole()).isEqualTo(Role.valueOf(role.toUpperCase())),
                () -> assertThat(member.getSex()).isEqualTo(Sex.valueOf(sex.toUpperCase())),
                () -> assertThat(member.getBirth()).isEqualTo(birth)
        );
    }

    @Test
    void login_success() {
        //given
        String id = "id";
        String username = "username";
        String password = "password";
        String role = "user";
        String sex = "male";
        String birth = "2024-07-30";

        authService.signup(new MemberSignupRequest(id, username, password, role, sex, birth));

        //when
        LoginRequest loginRequest = new LoginRequest(id, password);
        String token = authService.login(loginRequest);

        //then
        assertThat(token).isNotBlank();
    }

    @Test
    void login_without_signup_failure() {
        //given
        String id = "id";
        String password = "password";

        //when
        LoginRequest loginRequest = new LoginRequest(id, password);

        //then
        assertThatThrownBy(() -> authService.login(loginRequest))
                .isInstanceOf(RuntimeException.class);
    }
}