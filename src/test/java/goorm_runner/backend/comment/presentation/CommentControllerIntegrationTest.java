package goorm_runner.backend.comment.presentation;

import com.fasterxml.jackson.databind.ObjectMapper;
import goorm_runner.backend.comment.application.CommentService;
import goorm_runner.backend.comment.domain.Comment;
import goorm_runner.backend.comment.presentation.dto.CommentCreateRequest;
import goorm_runner.backend.member.domain.Member;
import goorm_runner.backend.member.security.application.AuthService;
import goorm_runner.backend.member.security.dto.LoginRequest;
import goorm_runner.backend.member.security.dto.MemberSignupRequest;
import goorm_runner.backend.post.application.PostService;
import goorm_runner.backend.post.domain.Post;
import goorm_runner.backend.post.domain.PostRepository;
import goorm_runner.backend.post.dto.PostCreateRequest;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static goorm_runner.backend.global.ErrorCode.COMMENT_NOT_FOUND;
import static goorm_runner.backend.global.ErrorCode.EMPTY_CONTENT;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class CommentControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private AuthService authService;

    @Autowired
    private PostService postService;

    @Autowired
    private CommentService commentService;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private EntityManager em;

    @BeforeEach
    void setUp() {
        postRepository.deleteAll();
    }

    @Test
    void create_success() throws Exception {
        //given
        String title = "Example title";
        String content = "<h1>Example</h1> Insert content here.";
        PostCreateRequest createRequest = new PostCreateRequest(title, content);

        String loginId = "test";
        String password = "password";

        String categoryName = "general";
        CommentCreateRequest request = new CommentCreateRequest("comment");

        //when
        Member member = authService.signup(new MemberSignupRequest(loginId, "test", password, "user", "male", "2000-01-01"));
        String token = authService.login(new LoginRequest(loginId, password));

        Post post = postService.create(createRequest, member.getId(), categoryName.toUpperCase());

        //then
        mockMvc.perform(post("/categories/general/posts/" + post.getId() + "/comments")
                        .header("Authorization", "Bearer " + token)
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON)
                ).andExpectAll(
                        status().isCreated(),
                        jsonPath("$.postId").value(post.getId()),
                        jsonPath("$.commentId").isNotEmpty(),
                        jsonPath("$.content").value(request.content()),
                        jsonPath("$.createdAt").isNotEmpty()
                );
    }

    @Test
    void create_with_empty_content_failure() throws Exception {
        //given
        String title = "Example title";
        String content = "<h1>Example</h1> Insert content here.";
        PostCreateRequest createRequest = new PostCreateRequest(title, content);

        String loginId = "test";
        String password = "password";

        String categoryName = "general";
        CommentCreateRequest request = new CommentCreateRequest("");

        //when
        Member member = authService.signup(new MemberSignupRequest(loginId, "test", password, "user", "male", "2000-01-01"));
        String token = authService.login(new LoginRequest(loginId, password));

        Post post = postService.create(createRequest, member.getId(), categoryName.toUpperCase());

        //then
        mockMvc.perform(post("/categories/general/posts/" + post.getId() + "/comments")
                        .header("Authorization", "Bearer " + token)
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.title").value(EMPTY_CONTENT.name()))
                .andExpect(jsonPath("$.message").value(EMPTY_CONTENT.getMessage()));
    }

    @Test
    void comment_to_deleted_post_is_not_allowed() throws Exception {
        //given
        String title = "Example title";
        String content = "<h1>Example</h1> Insert content here.";
        PostCreateRequest createRequest = new PostCreateRequest(title, content);

        String loginId = "test";
        String password = "password";

        String categoryName = "general";
        CommentCreateRequest request = new CommentCreateRequest("comment");

        //when
        Member member = authService.signup(new MemberSignupRequest(loginId, "test", password, "user", "male", "2000-01-01"));
        String token = authService.login(new LoginRequest(loginId, password));

        Post post = postService.create(createRequest, member.getId(), categoryName.toUpperCase());
        postService.delete(post.getId());
        em.flush();
        em.clear();

        //then
        mockMvc.perform(post("/categories/general/posts/" + post.getId() + "/comments")
                        .header("Authorization", "Bearer " + token)
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isNotFound());
    }

    @Test
    void read_success() throws Exception {
        //given
        String title = "Example title";
        String content = "<h1>Example</h1> Insert content here.";
        PostCreateRequest createRequest = new PostCreateRequest(title, content);

        String loginId = "test";
        String password = "password";

        String categoryName = "general";

        //when
        Member member = authService.signup(new MemberSignupRequest(loginId, "test", password, "user", "male", "2000-01-01"));
        String token = authService.login(new LoginRequest(loginId, password));

        Post post = postService.create(createRequest, member.getId(), categoryName.toUpperCase());
        Comment comment = commentService.create(member.getId(), post.getId(), "comment");

        //then
        mockMvc.perform(get("/categories/general/posts/" + post.getId() + "/comments/" + comment.getId())
                .header("Authorization", "Bearer " + token)
        ).andExpectAll(
                status().isOk(),
                jsonPath("$.commentId").value(comment.getId()),
                jsonPath("$.content").value(comment.getContent()),
                jsonPath("$.updatedAt").isNotEmpty()
        );
    }

    @Test
    void reading_non_existing_comment_failure() throws Exception {
        //given
        String title = "Example title";
        String content = "<h1>Example</h1> Insert content here.";
        PostCreateRequest createRequest = new PostCreateRequest(title, content);

        String loginId = "test";
        String password = "password";

        String categoryName = "general";

        //when
        Member member = authService.signup(new MemberSignupRequest(loginId, "test", password, "user", "male", "2000-01-01"));
        String token = authService.login(new LoginRequest(loginId, password));

        Post post = postService.create(createRequest, member.getId(), categoryName.toUpperCase());

        //then
        mockMvc.perform(get("/categories/general/posts/" + post.getId() + "/comments/" + -1L)
                .header("Authorization", "Bearer " + token)
        ).andExpectAll(
                status().isNotFound(),
                jsonPath("$.title").value(COMMENT_NOT_FOUND.name()),
                jsonPath("$.message").value(COMMENT_NOT_FOUND.getMessage())
        );
    }
}