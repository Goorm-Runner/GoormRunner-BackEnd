package goorm_runner.backend.postlike.presentation;

import goorm_runner.backend.global.ErrorCode;
import goorm_runner.backend.member.domain.Member;
import goorm_runner.backend.member.security.application.AuthService;
import goorm_runner.backend.member.security.dto.LoginRequest;
import goorm_runner.backend.member.security.dto.MemberSignupRequest;
import goorm_runner.backend.post.application.post.PostService;
import goorm_runner.backend.post.domain.model.Category;
import goorm_runner.backend.post.domain.model.Post;
import goorm_runner.backend.post.presentation.post.dto.PostCreateRequest;
import goorm_runner.backend.postlike.application.PostLikeService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class PostLikeControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PostLikeService postLikeService;

    @Autowired
    private AuthService authService;

    @Autowired
    private PostService postService;

    @Test
    void like_success() throws Exception {
        //given
        String title = "Example title";
        String content = "<h1>Example</h1> Insert content here.";
        PostCreateRequest request = new PostCreateRequest(title, content);

        String loginId = "test";
        String password = "password";

        //when
        Member member = authService.signup(new MemberSignupRequest(loginId, "test", password, "user", "male", "2000-01-01"));
        String token = authService.login(new LoginRequest(loginId, password));

        Post post = postService.create(title, content, member.getId(), Category.GENERAL);

        //then
        mockMvc.perform(post("/likes/posts/" + post.getId())
                        .header("Authorization", "Bearer " + token)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));
    }

    @Test
    void cannot_like_post_twice() throws Exception {
        //given
        String title = "Example title";
        String content = "<h1>Example</h1> Insert content here.";
        PostCreateRequest request = new PostCreateRequest(title, content);

        String loginId = "test";
        String password = "password";

        //when
        Member member = authService.signup(new MemberSignupRequest(loginId, "test", password, "user", "male", "2000-01-01"));
        String token = authService.login(new LoginRequest(loginId, password));

        Post post = postService.create(title, content, member.getId(), Category.GENERAL);

        postLikeService.likePost(post.getId(), member.getId());

        //then
        mockMvc.perform(post("/likes/posts/" + post.getId())
                        .header("Authorization", "Bearer " + token)
                )
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.title").value(ErrorCode.ALREADY_LIKED.name()))
                .andExpect(jsonPath("$.message").value(ErrorCode.ALREADY_LIKED.getMessage()));
    }

    @Test
    void count_success() throws Exception {
        //given
        String title = "Example title";
        String content = "<h1>Example</h1> Insert content here.";
        PostCreateRequest request = new PostCreateRequest(title, content);

        String loginId = "test";
        String password = "password";

        Member member = authService.signup(new MemberSignupRequest(loginId, "test", password, "user", "male", "2000-01-01"));
        authService.login(new LoginRequest(loginId, password));

        Post post = postService.create(title, content, member.getId(), Category.GENERAL);

        //when
        postLikeService.likePost(post.getId(), member.getId());

        //then
        mockMvc.perform(get("/likes/posts/" + post.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.postId").value(post.getId()))
                .andExpect(jsonPath("$.totalCount").value(1));
    }

    @Test
    void cancel_like_success() throws Exception {
        //given
        String title = "Example title";
        String content = "<h1>Example</h1> Insert content here.";
        PostCreateRequest request = new PostCreateRequest(title, content);

        String loginId = "test";
        String password = "password";

        Member member = authService.signup(new MemberSignupRequest(loginId, "test", password, "user", "male", "2000-01-01"));
        String token = authService.login(new LoginRequest(loginId, password));

        Post post = postService.create(title, content, member.getId(), Category.GENERAL);

        //when
        postLikeService.likePost(post.getId(), member.getId());

        //then
        mockMvc.perform(delete("/likes/posts/" + post.getId())
                        .header("Authorization", "Bearer " + token)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));
    }

    @Test
    void cancel_and_count() throws Exception {
        //given
        String title = "Example title";
        String content = "<h1>Example</h1> Insert content here.";
        PostCreateRequest request = new PostCreateRequest(title, content);

        String loginId = "test";
        String password = "password";

        Member member = authService.signup(new MemberSignupRequest(loginId, "test", password, "user", "male", "2000-01-01"));
        authService.login(new LoginRequest(loginId, password));

        Post post = postService.create(title, content, member.getId(), Category.GENERAL);
        postLikeService.likePost(post.getId(), member.getId());

        //when
        postLikeService.deletePostLike(post.getId(), member.getId());

        //then
        mockMvc.perform(get("/likes/posts/" + post.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.postId").value(post.getId()))
                .andExpect(jsonPath("$.totalCount").value(0));
    }

    @Test
    void failToCancelLikeIfNotExists() throws Exception {
        //given
        String title = "Example title";
        String content = "<h1>Example</h1> Insert content here.";
        PostCreateRequest request = new PostCreateRequest(title, content);

        String loginId = "test";
        String password = "password";

        Member member = authService.signup(new MemberSignupRequest(loginId, "test", password, "user", "male", "2000-01-01"));
        String token = authService.login(new LoginRequest(loginId, password));

        Post post = postService.create(title, content, member.getId(), Category.GENERAL);

        //when

        //then
        mockMvc.perform(delete("/likes/posts/" + post.getId())
                        .header("Authorization", "Bearer " + token)
                )
                .andExpect(jsonPath("$.title").value(ErrorCode.NOT_ALREADY_LIKED.name()))
                .andExpect(jsonPath("$.message").value(ErrorCode.NOT_ALREADY_LIKED.getMessage()));
    }
}