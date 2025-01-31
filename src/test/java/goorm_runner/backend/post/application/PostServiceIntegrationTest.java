package goorm_runner.backend.post.application;

import goorm_runner.backend.global.ErrorCode;
import goorm_runner.backend.post.application.exception.PostException;
import goorm_runner.backend.post.domain.Category;
import goorm_runner.backend.post.domain.Post;
import goorm_runner.backend.post.domain.PostRepository;
import goorm_runner.backend.post.dto.PostCreateRequest;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Transactional
public class PostServiceIntegrationTest {

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private PostService postService;

    @Autowired
    private PostReadService postReadService;

    @Autowired
    private PostRepository postRepository;

    @BeforeEach
    void setUp() {
        postRepository.deleteAll();
    }

    @Test
    void delete_success() {
        //given
        String title = "Example title";
        String content = "<h1>Example</h1> Insert content here.";
        PostCreateRequest createRequest = new PostCreateRequest(title, content);

        Long authorId = 1L;

        String categoryName = Category.GENERAL.name();

        Post post = postService.create(createRequest, authorId, categoryName);
        Long postId = post.getId();

        //when
        postService.delete(postId, authorId);

        //then
        assertThat(post.getDeletedAt()).isNotNull();
    }

    @Test
    void delete_failure() {
        //given
        String title = "Example title";
        String content = "<h1>Example</h1> Insert content here.";
        PostCreateRequest createRequest = new PostCreateRequest(title, content);

        Long authorId = 1L;

        String categoryName = Category.GENERAL.name();

        Post post = postService.create(createRequest, authorId, categoryName);
        Long postId = post.getId();

        //then
        assertThatThrownBy(() -> postService.delete(postId + 1, authorId))
                .isInstanceOf(PostException.class)
                .hasMessage(ErrorCode.POST_NOT_FOUND.getMessage());
    }

    @Test
    void delete_then_cannot_read() {
        //given
        String title = "Example title";
        String content = "<h1>Example</h1> Insert content here.";
        PostCreateRequest createRequest = new PostCreateRequest(title, content);

        Long authorId = 1L;

        String categoryName = Category.GENERAL.name();

        Post post = postService.create(createRequest, authorId, categoryName);
        Long postId = post.getId();

        //when
        postService.delete(postId, authorId);
        entityManager.flush();
        entityManager.clear();

        //then
        assertThatThrownBy(() -> postReadService.readPost(postId))
                .isInstanceOf(PostException.class)
                .hasMessage(ErrorCode.POST_NOT_FOUND.getMessage());
    }

    @Test
    void delete_then_cannot_read_page() {
        //given
        String title1 = "Example title";
        String title2 = "Example title2";
        String content = "<h1>Example</h1> Insert content here.";
        PostCreateRequest createRequest1 = new PostCreateRequest(title1, content);
        PostCreateRequest createRequest2 = new PostCreateRequest(title2, content);

        Long authorId = 1L;

        String categoryName = Category.GENERAL.name();

        Post post1 = postService.create(createRequest1, authorId, categoryName);
        postService.create(createRequest2, authorId, categoryName);

        //when
        postService.delete(post1.getId(), authorId);
        PageRequest pageRequest = PageRequest.of(0, 10);
        Page<Post> posts = postReadService.readPage(categoryName, pageRequest);

        //then
        assertThat(posts.getTotalElements()).isEqualTo(1);
        assertThat(posts.getContent().get(0).getTitle()).isEqualTo(title2);
    }
}
