package goorm_runner.backend.post.presentation;

import com.fasterxml.jackson.databind.ObjectMapper;
import goorm_runner.backend.global.ErrorCode;
import goorm_runner.backend.member.application.AuthorityRepository;
import goorm_runner.backend.member.domain.Authority;
import goorm_runner.backend.member.domain.Member;
import goorm_runner.backend.member.security.application.AuthService;
import goorm_runner.backend.member.security.dto.LoginRequest;
import goorm_runner.backend.member.security.dto.MemberSignupRequest;
import goorm_runner.backend.post.application.PostService;
import goorm_runner.backend.post.domain.Post;
import goorm_runner.backend.post.dto.PostCreateRequest;
import goorm_runner.backend.post.dto.PostUpdateRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class PostControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AuthService authService;

    @Autowired
    private PostService postService;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private AuthorityRepository authorityRepository;

    @Test
    void create_success() throws Exception {
        //given
        String title = "Example title";
        String content = "<h1>Example</h1> Insert content here.";
        PostCreateRequest request = new PostCreateRequest(title, content);

        String loginId = "test";
        String password = "password";

        //when
        authorityRepository.save(new Authority(1L, "read"));
        authService.signup(new MemberSignupRequest(loginId, "test", password, "user", "male", "2000-01-01"));
        String token = authService.login(new LoginRequest(loginId, password));

        //then
        mockMvc.perform(post("/categories/general/posts")
                        .header("Authorization", "Bearer " + token)
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.categoryName").value("GENERAL"))
                .andExpect(jsonPath("$.postId").isNotEmpty())
                .andExpect(jsonPath("$.title").value(title))
                .andExpect(jsonPath("$.content").value(content))
                .andExpect(jsonPath("$.createdAt").isNotEmpty());
    }

    @Test
    void create_with_empty_title_failure() throws Exception {
        //given
        String emptyTitle = "";
        String content = "<h1>Example</h1> Insert content here.";
        PostCreateRequest request = new PostCreateRequest(emptyTitle, content);

        String loginId = "test";
        String password = "password";

        //when
        authorityRepository.save(new Authority(1L, "read"));
        authService.signup(new MemberSignupRequest(loginId, "test", password, "user", "male", "2000-01-01"));
        String token = authService.login(new LoginRequest(loginId, password));

        //then
        mockMvc.perform(post("/categories/general/posts")
                        .header("Authorization", "Bearer " + token)
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.title").value(ErrorCode.EMPTY_TITLE.name()))
                .andExpect(jsonPath("$.message").value(ErrorCode.EMPTY_TITLE.getMessage()));
    }

    @Test
    void create_with_empty_content_failure() throws Exception {
        //given
        String title = "Example title";
        String emptyContent = "";
        PostCreateRequest request = new PostCreateRequest(title, emptyContent);

        String loginId = "test";
        String password = "password";

        //when
        authorityRepository.save(new Authority(1L, "read"));
        authService.signup(new MemberSignupRequest(loginId, "test", password, "user", "male", "2000-01-01"));
        String token = authService.login(new LoginRequest(loginId, password));

        //then
        mockMvc.perform(post("/categories/general/posts")
                        .header("Authorization", "Bearer " + token)
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.title").value(ErrorCode.EMPTY_CONTENT.name()))
                .andExpect(jsonPath("$.message").value(ErrorCode.EMPTY_CONTENT.getMessage()));
    }

    @Test
    void create_with_invalid_category_failure() throws Exception {
        //given
        String title = "Example title";
        String content = "<h1>Example</h1> Insert content here.";
        PostCreateRequest request = new PostCreateRequest(title, content);

        String loginId = "test";
        String password = "password";

        //when
        authorityRepository.save(new Authority(1L, "read"));
        authService.signup(new MemberSignupRequest(loginId, "test", password, "user", "male", "2000-01-01"));
        String token = authService.login(new LoginRequest(loginId, password));

        //then
        mockMvc.perform(post("/categories/generall/posts") // invalid category: generall
                        .header("Authorization", "Bearer " + token)
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.title").value(ErrorCode.INVALID_CATEGORY.name()))
                .andExpect(jsonPath("$.message").value(ErrorCode.INVALID_CATEGORY.getMessage()));
    }

    @Test
    void readTest() throws Exception {
        //given
        String title = "Example title";
        String content = "<h1>Example</h1> Insert content here.";
        PostCreateRequest createRequest = new PostCreateRequest(title, content);

        String loginId = "test";
        String password = "password";

        String categoryName = "general";

        //when
        authorityRepository.save(new Authority(1L, "read"));
        Member member = authService.signup(new MemberSignupRequest(loginId, "test", password, "user", "male", "2000-01-01"));
        String token = authService.login(new LoginRequest(loginId, password));

        Post post = postService.create(createRequest, member.getId(), categoryName.toUpperCase());

        //then
        mockMvc.perform(get("/categories/general/posts/" + post.getId())
                        .header("Authorization", "Bearer " + token)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.categoryName").isNotEmpty())
                .andExpect(jsonPath("$.postId").isNotEmpty())
                .andExpect(jsonPath("$.title").isNotEmpty())
                .andExpect(jsonPath("$.likeCount").isNotEmpty())
                .andExpect(jsonPath("$.content").isNotEmpty())
                .andExpect(jsonPath("$.createdAt").isNotEmpty())
                .andExpect(jsonPath("$.updatedAt").isNotEmpty());
    }

    @Test
    void readPageTest() throws Exception {
        //given
        String title = "Example title";
        String content = "<h1>Example</h1> Insert content here.";
        PostCreateRequest createRequest = new PostCreateRequest(title, content);

        String loginId = "test";
        String password = "password";

        String categoryName = "general";

        //when
        authorityRepository.save(new Authority(1L, "read"));
        Member member = authService.signup(new MemberSignupRequest(loginId, "test", password, "user", "male", "2000-01-01"));
        String token = authService.login(new LoginRequest(loginId, password));

        Post post = postService.create(createRequest, member.getId(), categoryName.toUpperCase());

        //then
        mockMvc.perform(get("/categories/general/posts?pageNumber=0&pageSize=10")
                        .header("Authorization", "Bearer " + token)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.overviews").isNotEmpty())
                .andExpect(jsonPath("$.responseMetaData").isNotEmpty());
    }

    @Test
    void update_success() throws Exception {
        //given
        String title = "Example title";
        String content = "<h1>Example</h1> Insert content here.";
        PostCreateRequest createRequest = new PostCreateRequest(title, content);

        String loginId = "test";
        String password = "password";
        String categoryName = "general";

        authorityRepository.save(new Authority(1L, "read"));
        Member member = authService.signup(new MemberSignupRequest(loginId, "test", password, "user", "male", "2000-01-01"));
        String token = authService.login(new LoginRequest(loginId, password));

        Post post = postService.create(createRequest, member.getId(), categoryName.toUpperCase());

        //when
        String updatedTitle = "Updated title";
        String updatedContent = "<h1>Example</h1> updated.";
        PostUpdateRequest request = new PostUpdateRequest(updatedTitle, updatedContent);

        //then
        mockMvc.perform(put("/categories/general/posts/" + post.getId())
                        .header("Authorization", "Bearer " + token)
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.categoryName").value("GENERAL"))
                .andExpect(jsonPath("$.postId").isNotEmpty())
                .andExpect(jsonPath("$.title").value(updatedTitle))
                .andExpect(jsonPath("$.content").value(updatedContent))
                .andExpect(jsonPath("$.createdAt").isNotEmpty())
                .andExpect(jsonPath("$.updatedAt").isNotEmpty());
    }

    @Test
    void deleteTest() throws Exception {
        //given
        String title = "Example title";
        String content = "<h1>Example</h1> Insert content here.";
        PostCreateRequest createRequest = new PostCreateRequest(title, content);

        String loginId = "test";
        String password = "password";

        String categoryName = "general";

        //when
        authorityRepository.save(new Authority(1L, "read"));
        Member member = authService.signup(new MemberSignupRequest(loginId, "test", password, "user", "male", "2000-01-01"));
        String token = authService.login(new LoginRequest(loginId, password));

        Post post = postService.create(createRequest, member.getId(), categoryName.toUpperCase());

        //then
        mockMvc.perform(delete("/categories/general/posts/" + post.getId())
                        .header("Authorization", "Bearer " + token)
                )
                .andExpect(status().isNoContent());
    }
}