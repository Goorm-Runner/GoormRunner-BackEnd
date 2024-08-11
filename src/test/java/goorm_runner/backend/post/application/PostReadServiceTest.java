package goorm_runner.backend.post.application;

import goorm_runner.backend.post.application.post.PostReadService;
import goorm_runner.backend.post.application.post.PostService;
import goorm_runner.backend.post.application.post.exception.PostException;
import goorm_runner.backend.post.domain.PostRepository;
import goorm_runner.backend.post.domain.model.Category;
import goorm_runner.backend.post.domain.model.Post;
import goorm_runner.backend.post.dto.PostCreateRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static goorm_runner.backend.global.ErrorCode.POST_NOT_FOUND;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

@SpringBootTest
@Transactional
class PostReadServiceTest {

    @Autowired
    private PostReadService postReadService;

    @Autowired
    private PostService postService;

    @Autowired
    private PostRepository postRepository;

    @BeforeEach
    void setUp() {
        postRepository.deleteAll();
    }

    @Test
    void read_success() {
        //given
        String title = "Example title";
        String content = "<h1>Example</h1> Insert content here.";
        PostCreateRequest createRequest = new PostCreateRequest(title, content);

        Long authorId = 1L;

        Post post = postService.create(createRequest, authorId, Category.GENERAL);

        //when
        Post findPost = postReadService.readPost(post.getId());

        //then
        assertThat(post == findPost).isTrue();
    }

    @Test
    void read_failure() {
        String title = "Example title";
        String content = "<h1>Example</h1> Insert content here.";
        PostCreateRequest createRequest = new PostCreateRequest(title, content);

        Long authorId = 1L;

        Post post = postService.create(createRequest, authorId, Category.GENERAL);

        //when
        Long wrongId = post.getId() + 1;

        //then
        assertThatThrownBy(() -> postReadService.readPost(wrongId))
                .isInstanceOf(PostException.class)
                .hasMessage(POST_NOT_FOUND.getMessage());
    }

    @Test
    void read_page_success() {
        //given
        String title = "Example title";
        String content = "<h1>Example</h1> Insert content here.";
        PostCreateRequest createRequest = new PostCreateRequest(title, content);

        Long authorId = 1L;

        postService.create(createRequest, authorId, Category.GENERAL);

        //when
        PageRequest pageRequest = PageRequest.of(0, 10);
        Page<Post> page = postReadService.readPage(Category.GENERAL, pageRequest);

        //then
        List<Post> contents = page.getContent();
        assertAll(
                () -> assertThat(contents.size()).isEqualTo(1),
                () -> assertThat(page.getTotalPages()).isEqualTo(1),
                () -> assertThat(page.getNumber()).isEqualTo(0),
                () -> assertThat(page.getTotalPages()).isEqualTo(1),
                () -> assertThat(page.isFirst()).isTrue(),
                () -> assertThat(page.hasNext()).isFalse()
        );
    }
}