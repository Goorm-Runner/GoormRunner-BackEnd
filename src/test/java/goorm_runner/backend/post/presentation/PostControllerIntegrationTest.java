package goorm_runner.backend.post.presentation;

import com.fasterxml.jackson.databind.ObjectMapper;
import goorm_runner.backend.member.domain.Member;
import goorm_runner.backend.member.security.application.AuthService;
import goorm_runner.backend.member.security.dto.LoginRequest;
import goorm_runner.backend.member.security.dto.MemberSignupRequest;
import goorm_runner.backend.post.application.PostService;
import goorm_runner.backend.post.domain.Category;
import goorm_runner.backend.post.domain.Post;
import goorm_runner.backend.post.domain.PostRepository;
import goorm_runner.backend.post.dto.PostCreateRequest;
import goorm_runner.backend.post.dto.PostUpdateRequest;
import goorm_runner.backend.postlike.application.PostLikeService;
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
    private PostLikeService postLikeService;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private PostRepository postRepository;

    @BeforeEach
    void setUp() {
        postRepository.deleteAll();
    }

    @Test
    void create_success() throws Exception {
        //given
        String title = "Example title";
        String content = "<h1>Example</h1> Insert content here.";
        PostCreateRequest request = new PostCreateRequest(title, content);

        String loginId = "test";
        String password = "password";

        //when
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
        authService.signup(new MemberSignupRequest(loginId, "test", password, "user", "male", "2000-01-01"));
        String token = authService.login(new LoginRequest(loginId, password));

        //then
        mockMvc.perform(post("/categories/general/posts")
                        .header("Authorization", "Bearer " + token)
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.title").value(EMPTY_TITLE.name()))
                .andExpect(jsonPath("$.message").value(EMPTY_TITLE.getMessage()));
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
        authService.signup(new MemberSignupRequest(loginId, "test", password, "user", "male", "2000-01-01"));
        String token = authService.login(new LoginRequest(loginId, password));

        //then
        mockMvc.perform(post("/categories/general/posts")
                        .header("Authorization", "Bearer " + token)
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.title").value(EMPTY_CONTENT.name()))
                .andExpect(jsonPath("$.message").value(EMPTY_CONTENT.getMessage()));
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
        authService.signup(new MemberSignupRequest(loginId, "test", password, "user", "male", "2000-01-01"));
        String token = authService.login(new LoginRequest(loginId, password));

        //then
        mockMvc.perform(post("/categories/generall/posts") // invalid category: generall
                        .header("Authorization", "Bearer " + token)
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.title").value(INVALID_CATEGORY.name()))
                .andExpect(jsonPath("$.message").value(INVALID_CATEGORY.getMessage()));
    }

    @Test
    void read_success() throws Exception {
        //given
        String title = "Example title";
        String content = "<h1>Example</h1> Insert content here.";
        PostCreateRequest createRequest = new PostCreateRequest(title, content);

        String loginId = "test";
        String password = "password";

        String categoryName = Category.GENERAL.name();

        //when
        Member member = authService.signup(new MemberSignupRequest(loginId, "test", password, "user", "male", "2000-01-01"));
        authService.login(new LoginRequest(loginId, password));

        Post post = postService.create(createRequest, member.getId(), categoryName.toUpperCase());

        //then
        mockMvc.perform(get("/categories/general/posts/" + post.getId()))
                .andExpect(status().isOk())
                .andExpectAll(
                        jsonPath("$.categoryName").value(Category.GENERAL.name()),
                        jsonPath("$.postId").value(post.getId()),
                        jsonPath("$.title").value(post.getTitle()),
                        jsonPath("$.likeCount").value(0),
                        jsonPath("$.content").value(post.getContent()),
                        jsonPath("$.createdAt").value(post.getCreatedAt().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)),
                        jsonPath("$.updatedAt").value(post.getUpdatedAt().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))
                );
    }

    @Test
    void read_after_like() throws Exception {
        //given
        String title = "Example title";
        String content = "<h1>Example</h1> Insert content here.";
        PostCreateRequest createRequest = new PostCreateRequest(title, content);

        String loginId = "test";
        String password = "password";

        String categoryName = Category.GENERAL.name();

        //when
        Member member = authService.signup(new MemberSignupRequest(loginId, "test", password, "user", "male", "2000-01-01"));
        authService.login(new LoginRequest(loginId, password));

        Post post = postService.create(createRequest, member.getId(), categoryName.toUpperCase());
        postLikeService.likePost(post.getId(), member.getId());

        //then
        mockMvc.perform(get("/categories/general/posts/" + post.getId()))
                .andExpect(status().isOk())
                .andExpectAll(
                        jsonPath("$.categoryName").value(Category.GENERAL.name()),
                        jsonPath("$.postId").value(post.getId()),
                        jsonPath("$.title").value(post.getTitle()),
                        jsonPath("$.likeCount").value(1),
                        jsonPath("$.content").value(post.getContent()),
                        jsonPath("$.createdAt").value(post.getCreatedAt().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)),
                        jsonPath("$.updatedAt").value(post.getUpdatedAt().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))
                );
    }

    @Test
    void read_after_cancel_like() throws Exception {
        //given
        String title = "Example title";
        String content = "<h1>Example</h1> Insert content here.";
        PostCreateRequest createRequest = new PostCreateRequest(title, content);

        String loginId = "test";
        String password = "password";

        String categoryName = Category.GENERAL.name();

        //when
        Member member = authService.signup(new MemberSignupRequest(loginId, "test", password, "user", "male", "2000-01-01"));
        authService.login(new LoginRequest(loginId, password));

        Post post = postService.create(createRequest, member.getId(), categoryName.toUpperCase());
        postLikeService.likePost(post.getId(), member.getId());
        postLikeService.deletePostLike(post.getId(), member.getId());

        //then
        mockMvc.perform(get("/categories/general/posts/" + post.getId()))
                .andExpect(status().isOk())
                .andExpectAll(
                        jsonPath("$.categoryName").value(Category.GENERAL.name()),
                        jsonPath("$.postId").value(post.getId()),
                        jsonPath("$.title").value(post.getTitle()),
                        jsonPath("$.likeCount").value(0),
                        jsonPath("$.content").value(post.getContent()),
                        jsonPath("$.createdAt").value(post.getCreatedAt().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)),
                        jsonPath("$.updatedAt").value(post.getUpdatedAt().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))
                );
    }

    @Test
    void read_failure() throws Exception {
        //given
        String title = "Example title";
        String content = "<h1>Example</h1> Insert content here.";
        PostCreateRequest createRequest = new PostCreateRequest(title, content);

        String loginId = "test";
        String password = "password";

        String categoryName = Category.GENERAL.name();

        //when
        Member member = authService.signup(new MemberSignupRequest(loginId, "test", password, "user", "male", "2000-01-01"));
        authService.login(new LoginRequest(loginId, password));

        Post post = postService.create(createRequest, member.getId(), categoryName.toUpperCase());

        //then
        mockMvc.perform(get("/categories/general/posts/" + post.getId() + 1))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.title").value(POST_NOT_FOUND.name()))
                .andExpect(jsonPath("$.message").value(POST_NOT_FOUND.getMessage()));
    }

    @Test
    void readPage_success() throws Exception {
        //given
        String title = "Example title";
        String content = "<h1>Example</h1> Insert content here.";
        PostCreateRequest createRequest = new PostCreateRequest(title, content);

        String loginId = "test";
        String password = "password";

        String categoryName = Category.GENERAL.name();

        //when
        Member member = authService.signup(new MemberSignupRequest(loginId, "test", password, "user", "male", "2000-01-01"));
        authService.login(new LoginRequest(loginId, password));

        Post post = postService.create(createRequest, member.getId(), categoryName.toUpperCase());

        //then
        mockMvc.perform(get("/categories/general/posts?pageNumber=0&pageSize=10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.overviews.size()").value(1))
                .andExpectAll(
                        jsonPath("$.overviews[0].categoryName").value(categoryName),
                        jsonPath("$.overviews[0].postId").value(post.getId()),
                        jsonPath("$.overviews[0].title").value(post.getTitle()),
                        jsonPath("$.overviews[0].createdAt").value(post.getCreatedAt().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)),
                        jsonPath("$.overviews[0].updatedAt").value(post.getUpdatedAt().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)),
                        jsonPath("$.overviews[0].authorName").value(member.getNickname()),
                        jsonPath("$.overviews[0].likeCount").value(0)
                )
                .andExpectAll(
                        jsonPath("$.responseMetaData.number").value(0),
                        jsonPath("$.responseMetaData.size").value(10),
                        jsonPath("$.responseMetaData.isFirst").value(true),
                        jsonPath("$.responseMetaData.isLast").value(true),
                        jsonPath("$.responseMetaData.hasNext").value(false),
                        jsonPath("$.responseMetaData.hasPrevious").value(false)
                );
    }

    @Test
    void readPage_likeCount() throws Exception {
        //given
        String title = "Example title";
        String content = "<h1>Example</h1> Insert content here.";
        PostCreateRequest createRequest = new PostCreateRequest(title, content);

        String loginId = "test";
        String password = "password";

        String categoryName = Category.GENERAL.name();

        //when
        Member member = authService.signup(new MemberSignupRequest(loginId, "test", password, "user", "male", "2000-01-01"));
        authService.login(new LoginRequest(loginId, password));

        Post post = postService.create(createRequest, member.getId(), categoryName.toUpperCase());
        postLikeService.likePost(post.getId(), member.getId());

        //then
        mockMvc.perform(get("/categories/general/posts?pageNumber=0&pageSize=10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.overviews.size()").value(1))
                .andExpectAll(
                        jsonPath("$.overviews[0].categoryName").value(categoryName),
                        jsonPath("$.overviews[0].postId").value(post.getId()),
                        jsonPath("$.overviews[0].title").value(post.getTitle()),
                        jsonPath("$.overviews[0].createdAt").value(post.getCreatedAt().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)),
                        jsonPath("$.overviews[0].updatedAt").value(post.getUpdatedAt().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)),
                        jsonPath("$.overviews[0].authorName").value(member.getNickname()),
                        jsonPath("$.overviews[0].likeCount").value(1)
                )
                .andExpectAll(
                        jsonPath("$.responseMetaData.number").value(0),
                        jsonPath("$.responseMetaData.size").value(10),
                        jsonPath("$.responseMetaData.isFirst").value(true),
                        jsonPath("$.responseMetaData.isLast").value(true),
                        jsonPath("$.responseMetaData.hasNext").value(false),
                        jsonPath("$.responseMetaData.hasPrevious").value(false)
                );
    }

    @Test
    void readPage_after_cancel_like() throws Exception {
        //given
        String title = "Example title";
        String content = "<h1>Example</h1> Insert content here.";
        PostCreateRequest createRequest = new PostCreateRequest(title, content);

        String loginId = "test";
        String password = "password";

        String categoryName = Category.GENERAL.name();

        //when
        Member member = authService.signup(new MemberSignupRequest(loginId, "test", password, "user", "male", "2000-01-01"));
        authService.login(new LoginRequest(loginId, password));

        Post post = postService.create(createRequest, member.getId(), categoryName.toUpperCase());
        postLikeService.likePost(post.getId(), member.getId());
        postLikeService.deletePostLike(post.getId(), member.getId());

        //then
        mockMvc.perform(get("/categories/general/posts?pageNumber=0&pageSize=10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.overviews.size()").value(1))
                .andExpectAll(
                        jsonPath("$.overviews[0].categoryName").value(categoryName),
                        jsonPath("$.overviews[0].postId").value(post.getId()),
                        jsonPath("$.overviews[0].title").value(post.getTitle()),
                        jsonPath("$.overviews[0].createdAt").value(post.getCreatedAt().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)),
                        jsonPath("$.overviews[0].updatedAt").value(post.getUpdatedAt().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)),
                        jsonPath("$.overviews[0].authorName").value(member.getNickname()),
                        jsonPath("$.overviews[0].likeCount").value(0)
                )
                .andExpectAll(
                        jsonPath("$.responseMetaData.number").value(0),
                        jsonPath("$.responseMetaData.size").value(10),
                        jsonPath("$.responseMetaData.isFirst").value(true),
                        jsonPath("$.responseMetaData.isLast").value(true),
                        jsonPath("$.responseMetaData.hasNext").value(false),
                        jsonPath("$.responseMetaData.hasPrevious").value(false)
                );
    }

    @Test
    void readPage_with_wrong_categoryName_failure() throws Exception {
        //given
        String title = "Example title";
        String content = "<h1>Example</h1> Insert content here.";
        PostCreateRequest createRequest = new PostCreateRequest(title, content);

        String loginId = "test";
        String password = "password";

        String categoryName = Category.GENERAL.name();

        //when
        Member member = authService.signup(new MemberSignupRequest(loginId, "test", password, "user", "male", "2000-01-01"));
        authService.login(new LoginRequest(loginId, password));

        postService.create(createRequest, member.getId(), categoryName.toUpperCase());

        //then
        mockMvc.perform(get("/categories/generall/posts?pageNumber=0&pageSize=10")) // wrong categoryName: generall
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.title").value(INVALID_CATEGORY.name()))
                .andExpect(jsonPath("$.message").value(INVALID_CATEGORY.getMessage()));
    }

    @Test
    void update_success() throws Exception {
        //given
        String title = "Example title";
        String content = "<h1>Example</h1> Insert content here.";
        PostCreateRequest createRequest = new PostCreateRequest(title, content);

        String loginId = "test";
        String password = "password";
        String categoryName = Category.GENERAL.name();

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
    void update_success_with_invalid_postId_failure() throws Exception {
        //given
        String title = "Example title";
        String content = "<h1>Example</h1> Insert content here.";
        PostCreateRequest createRequest = new PostCreateRequest(title, content);

        String loginId = "test";
        String password = "password";
        String categoryName = Category.GENERAL.name();

        Member member = authService.signup(new MemberSignupRequest(loginId, "test", password, "user", "male", "2000-01-01"));
        String token = authService.login(new LoginRequest(loginId, password));

        Post post = postService.create(createRequest, member.getId(), categoryName.toUpperCase());

        //when
        String updatedTitle = "Updated title";
        String updatedContent = "<h1>Example</h1> updated.";
        PostUpdateRequest request = new PostUpdateRequest(updatedTitle, updatedContent);

        //then
        mockMvc.perform(put("/categories/general/posts/" + post.getId() + 1)
                        .header("Authorization", "Bearer " + token)
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.title").value(POST_NOT_FOUND.name()))
                .andExpect(jsonPath("$.message").value(POST_NOT_FOUND.getMessage()));
    }

    @Test
    void update_success_with_empty_title_failure() throws Exception {
        //given
        String title = "Example title";
        String content = "<h1>Example</h1> Insert content here.";
        PostCreateRequest createRequest = new PostCreateRequest(title, content);

        String loginId = "test";
        String password = "password";
        String categoryName = Category.GENERAL.name();

        Member member = authService.signup(new MemberSignupRequest(loginId, "test", password, "user", "male", "2000-01-01"));
        String token = authService.login(new LoginRequest(loginId, password));

        Post post = postService.create(createRequest, member.getId(), categoryName.toUpperCase());

        //when
        String emptyTitle = "";
        String updatedContent = "<h1>Example</h1> updated.";
        PostUpdateRequest request = new PostUpdateRequest(emptyTitle, updatedContent);

        //then
        mockMvc.perform(put("/categories/general/posts/" + post.getId())
                        .header("Authorization", "Bearer " + token)
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.title").value(EMPTY_TITLE.name()))
                .andExpect(jsonPath("$.message").value(EMPTY_TITLE.getMessage()));
    }

    @Test
    void update_success_with_empty_content_failure() throws Exception {
        //given
        String title = "Example title";
        String content = "<h1>Example</h1> Insert content here.";
        PostCreateRequest createRequest = new PostCreateRequest(title, content);

        String loginId = "test";
        String password = "password";
        String categoryName = Category.GENERAL.name();

        Member member = authService.signup(new MemberSignupRequest(loginId, "test", password, "user", "male", "2000-01-01"));
        String token = authService.login(new LoginRequest(loginId, password));

        Post post = postService.create(createRequest, member.getId(), categoryName.toUpperCase());

        //when
        String updatedTitle = "Updated title";
        String emptyContent = "";
        PostUpdateRequest request = new PostUpdateRequest(updatedTitle, emptyContent);

        //then
        mockMvc.perform(put("/categories/general/posts/" + post.getId())
                        .header("Authorization", "Bearer " + token)
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.title").value(EMPTY_CONTENT.name()))
                .andExpect(jsonPath("$.message").value(EMPTY_CONTENT.getMessage()));
    }

    @Test
    void delete_success() throws Exception {
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
        mockMvc.perform(delete("/categories/general/posts/" + post.getId())
                        .header("Authorization", "Bearer " + token)
                )
                .andExpect(status().isNoContent());
    }

    @Test
    void delete_failure() throws Exception {
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
        mockMvc.perform(delete("/categories/general/posts/" + post.getId() + 1)
                        .header("Authorization", "Bearer " + token)
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.title").value(POST_NOT_FOUND.name()))
                .andExpect(jsonPath("$.message").value(POST_NOT_FOUND.getMessage()));
    }

    @Test
    void delete_then_read_failure() throws Exception {
        //given
        String title = "Example title";
        String content = "<h1>Example</h1> Insert content here.";
        PostCreateRequest createRequest = new PostCreateRequest(title, content);

        String loginId = "test";
        String password = "password";

        String categoryName = "general";

        //when
        Member member = authService.signup(new MemberSignupRequest(loginId, "test", password, "user", "male", "2000-01-01"));
        Long authorId = member.getId();
        String token = authService.login(new LoginRequest(loginId, password));

        Post post = postService.create(createRequest, member.getId(), categoryName.toUpperCase());
        postService.delete(post.getId(), authorId);
        em.flush();
        em.clear();

        //then
        mockMvc.perform(get("/categories/general/posts/" + post.getId())
                        .header("Authorization", "Bearer " + token)
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.title").value(POST_NOT_FOUND.name()))
                .andExpect(jsonPath("$.message").value(POST_NOT_FOUND.getMessage()));
    }

    @Test
    void delete_then_read_page() throws Exception {
        //given
        String title1 = "title1";
        String content1 = "content1";
        PostCreateRequest createRequest1 = new PostCreateRequest(title1, content1);

        String title2 = "title1";
        String content2 = "content1";
        PostCreateRequest createRequest2 = new PostCreateRequest(title2, content2);

        String loginId = "test";
        String password = "password";

        String categoryName = "general";

        //when
        Member member = authService.signup(new MemberSignupRequest(loginId, "test", password, "user", "male", "2000-01-01"));
        Long authorId = member.getId();
        String token = authService.login(new LoginRequest(loginId, password));

        Post post1 = postService.create(createRequest1, member.getId(), categoryName.toUpperCase());
        postService.create(createRequest2, member.getId(), categoryName.toUpperCase());

        postService.delete(post1.getId(), authorId); // post2만 남아있다.
        em.flush();
        em.clear();

        //then
        mockMvc.perform(get("/categories/general/posts?pageNumber=0&pageSize=10")
                        .header("Authorization", "Bearer " + token)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.overviews.size()").value(1))
                .andExpect(jsonPath("$.overviews[0].title").value(title2))
                .andExpectAll(
                        jsonPath("$.responseMetaData.number").value(0),
                        jsonPath("$.responseMetaData.size").value(10),
                        jsonPath("$.responseMetaData.isFirst").value(true),
                        jsonPath("$.responseMetaData.isLast").value(true),
                        jsonPath("$.responseMetaData.hasNext").value(false),
                        jsonPath("$.responseMetaData.hasPrevious").value(false)
                );
    }
}