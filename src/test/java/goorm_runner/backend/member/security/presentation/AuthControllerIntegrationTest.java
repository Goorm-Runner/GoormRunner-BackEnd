package goorm_runner.backend.member.security.presentation;

import com.fasterxml.jackson.databind.ObjectMapper;
import goorm_runner.backend.member.application.AuthorityRepository;
import goorm_runner.backend.member.domain.Authority;
import goorm_runner.backend.member.security.application.AuthService;
import goorm_runner.backend.member.security.dto.LoginRequest;
import goorm_runner.backend.member.security.dto.MemberSignupRequest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class AuthControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

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
    void signup_success() throws Exception {
        //given
        String id = "id";
        String username = "username";
        String password = "password";
        String role = "user";
        String sex = "male";
        String birth = "2024-07-30";

        //when
        MemberSignupRequest request = new MemberSignupRequest(id, username, password, role, sex, birth);

        //then
        mockMvc.perform(post("/api/auth/signup")
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk());
    }

    @Test
    void login_success() throws Exception {
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

        //then
        mockMvc.perform(post("/api/auth/login")
                        .content(objectMapper.writeValueAsString(loginRequest))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").isNotEmpty());
    }
}