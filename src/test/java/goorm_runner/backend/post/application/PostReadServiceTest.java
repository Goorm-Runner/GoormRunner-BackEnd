package goorm_runner.backend.post.application;

import goorm_runner.backend.post.application.exception.PostException;
import goorm_runner.backend.post.domain.Category;
import goorm_runner.backend.post.domain.Post;
import goorm_runner.backend.post.domain.PostRepository;
import goorm_runner.backend.post.dto.PostCreateRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static goorm_runner.backend.global.ErrorCode.INVALID_CATEGORY;
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

        String categoryName = Category.GENERAL.name();

        Post post = postService.create(createRequest, authorId, categoryName);

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

        String categoryName = Category.GENERAL.name();

        Post post = postService.create(createRequest, authorId, categoryName);

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

        String categoryName = Category.GENERAL.name();

        postService.create(createRequest, authorId, categoryName);

        //when
        PageRequest pageRequest = PageRequest.of(0, 10);
        Page<Post> page = postReadService.readPage(categoryName, pageRequest);

        //then
        List<Post> contents = page.getContent();
        assertAll(
                () -> assertThat(contents.size()).isEqualTo(1),
                () -> assertThat(page.getTotalPages()).isEqualTo(1),
                () -> assertThat(page.getNumber()).isEqualTo(0),
                () -> assertThat(page.getTotalPages()).isEqualTo(1),
                () -> assertThat(page.isFirst()).isTrue(),
                () ->assertThat(page.hasNext()).isFalse()
        );
    }

    @Test
    void read_page_with_wrong_categoryName_failure() {
        //given
        String title = "Example title";
        String content = "<h1>Example</h1> Insert content here.";
        PostCreateRequest createRequest = new PostCreateRequest(title, content);

        Long authorId = 1L;

        String categoryName = Category.GENERAL.name();

        postService.create(createRequest, authorId, categoryName);

        //when
        PageRequest pageRequest = PageRequest.of(0, 10);
        String wrongCategoryName = "GENERALL";

        //then
        assertThatThrownBy(() -> postReadService.readPage(wrongCategoryName, pageRequest))
                .isInstanceOf(PostException.class)
                .hasMessage(INVALID_CATEGORY.getMessage());
    }
}