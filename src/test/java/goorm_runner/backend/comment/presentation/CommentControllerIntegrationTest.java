package goorm_runner.backend.comment.presentation;

import com.fasterxml.jackson.databind.ObjectMapper;
import goorm_runner.backend.member.domain.Member;
import goorm_runner.backend.member.security.application.AuthService;
import goorm_runner.backend.member.security.dto.LoginRequest;
import goorm_runner.backend.member.security.dto.MemberSignupRequest;
import goorm_runner.backend.post.application.comment.CommentService;
import goorm_runner.backend.post.application.post.PostService;
import goorm_runner.backend.post.domain.PostRepository;
import goorm_runner.backend.post.domain.model.Category;
import goorm_runner.backend.post.domain.model.Comment;
import goorm_runner.backend.post.domain.model.Post;
import goorm_runner.backend.post.dto.PostCreateRequest;
import goorm_runner.backend.post.presentation.comment.dto.CommentCreateRequest;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.time.format.DateTimeFormatter;

import static goorm_runner.backend.global.ErrorCode.*;
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

        CommentCreateRequest request = new CommentCreateRequest("comment");

        //when
        Member member = authService.signup(new MemberSignupRequest(loginId, "test", password, "user", "male", "2000-01-01"));
        String token = authService.login(new LoginRequest(loginId, password));

        Post post = postService.create(title, content, member.getId(), Category.GENERAL);

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

        CommentCreateRequest request = new CommentCreateRequest("");

        //when
        Member member = authService.signup(new MemberSignupRequest(loginId, "test", password, "user", "male", "2000-01-01"));
        String token = authService.login(new LoginRequest(loginId, password));

        Post post = postService.create(title, content, member.getId(), Category.GENERAL);

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

        CommentCreateRequest request = new CommentCreateRequest("comment");

        //when
        Member member = authService.signup(new MemberSignupRequest(loginId, "test", password, "user", "male", "2000-01-01"));
        String token = authService.login(new LoginRequest(loginId, password));

        Post post = postService.create(title, content, member.getId(), Category.GENERAL);
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

        //when
        Member member = authService.signup(new MemberSignupRequest(loginId, "test", password, "user", "male", "2000-01-01"));
        String token = authService.login(new LoginRequest(loginId, password));

        Post post = postService.create(title, content, member.getId(), Category.GENERAL);
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
    void reading_non_existent_comment_failure() throws Exception {
        //given
        String title = "Example title";
        String content = "<h1>Example</h1> Insert content here.";
        PostCreateRequest createRequest = new PostCreateRequest(title, content);

        String loginId = "test";
        String password = "password";

        //when
        Member member = authService.signup(new MemberSignupRequest(loginId, "test", password, "user", "male", "2000-01-01"));
        String token = authService.login(new LoginRequest(loginId, password));

        Post post = postService.create(title, content, member.getId(), Category.GENERAL);

        //then
        mockMvc.perform(get("/categories/general/posts/" + post.getId() + "/comments/" + -1L)
                .header("Authorization", "Bearer " + token)
        ).andExpectAll(
                status().isNotFound(),
                jsonPath("$.title").value(COMMENT_NOT_FOUND.name()),
                jsonPath("$.message").value(COMMENT_NOT_FOUND.getMessage())
        );
    }

    @Test
    void reading_comment_after_deleting_post_is_not_allowed() throws Exception {
        //given
        String title = "Example title";
        String content = "<h1>Example</h1> Insert content here.";
        PostCreateRequest createRequest = new PostCreateRequest(title, content);

        String loginId = "test";
        String password = "password";

        //when
        Member member = authService.signup(new MemberSignupRequest(loginId, "test", password, "user", "male", "2000-01-01"));
        String token = authService.login(new LoginRequest(loginId, password));

        Post post = postService.create(title, content, member.getId(), Category.GENERAL);
        Comment comment = commentService.create(member.getId(), post.getId(), "comment");

        postService.delete(post.getId());
        em.flush();
        em.clear();

        //then
        mockMvc.perform(get("/categories/general/posts/" + post.getId() + "/comments/" + comment.getId())
                .header("Authorization", "Bearer " + token)
        ).andExpectAll(
                status().isNotFound(),
                jsonPath("$.title").value(POST_NOT_FOUND.name()),
                jsonPath("$.message").value(POST_NOT_FOUND.getMessage())
        );
    }

    @Test
    void readPage_success() throws Exception {
        //given
        String title = "Example title";
        String content = "<h1>Example</h1> Insert content here.";
        PostCreateRequest createRequest = new PostCreateRequest(title, content);

        String loginId = "test";
        String password = "password";

        //when
        Member member = authService.signup(new MemberSignupRequest(loginId, "test", password, "user", "male", "2000-01-01"));
        String token = authService.login(new LoginRequest(loginId, password));

        Post post = postService.create(title, content, member.getId(), Category.GENERAL);
        Comment comment = commentService.create(member.getId(), post.getId(), "comment");

        //then
        mockMvc.perform(get("/categories/general/posts/" + post.getId() + "/comments?pageNumber=0&pageSize=10")
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.overviews.size()").value(1))
                .andExpectAll(
                        jsonPath("$.overviews[0].postId").value(post.getId()),
                        jsonPath("$.overviews[0].commentId").value(comment.getId()),
                        jsonPath("$.overviews[0].content").value(comment.getContent()),
                        jsonPath("$.overviews[0].createdAt").value(comment.getCreatedAt().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)),
                        jsonPath("$.overviews[0].updatedAt").value(comment.getUpdatedAt().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)),
                        jsonPath("$.overviews[0].authorName").value(member.getNickname())
                )
                .andExpectAll(
                        jsonPath("$.pageMetaData.number").value(0),
                        jsonPath("$.pageMetaData.size").value(10),
                        jsonPath("$.pageMetaData.isFirst").value(true),
                        jsonPath("$.pageMetaData.isLast").value(true),
                        jsonPath("$.pageMetaData.hasNext").value(false),
                        jsonPath("$.pageMetaData.hasPrevious").value(false)
                );
    }

    @Test
    void reading_page_after_deleting_post_is_not_allowed() throws Exception {
        //given
        String title = "Example title";
        String content = "<h1>Example</h1> Insert content here.";
        PostCreateRequest createRequest = new PostCreateRequest(title, content);

        String loginId = "test";
        String password = "password";

        //when
        Member member = authService.signup(new MemberSignupRequest(loginId, "test", password, "user", "male", "2000-01-01"));
        String token = authService.login(new LoginRequest(loginId, password));

        Post post = postService.create(title, content, member.getId(), Category.GENERAL);
        commentService.create(member.getId(), post.getId(), "comment");

        postService.delete(post.getId());
        em.flush();
        em.clear();

        //then
        mockMvc.perform(get("/categories/general/posts/" + post.getId() + "/comments?pageNumber=0&pageSize=10")
                        .header("Authorization", "Bearer " + token)
                ).andExpectAll(
                status().isNotFound(),
                jsonPath("$.title").value(POST_NOT_FOUND.name()),
                jsonPath("$.message").value(POST_NOT_FOUND.getMessage())
        );
    }
}