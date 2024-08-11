package goorm_runner.backend.post.application;

import goorm_runner.backend.post.application.post.PostService;
import goorm_runner.backend.post.application.post.exception.PostException;
import goorm_runner.backend.post.domain.PostRepository;
import goorm_runner.backend.post.domain.model.Category;
import goorm_runner.backend.post.domain.model.Post;
import goorm_runner.backend.post.presentation.post.dto.PostCreateRequest;
import goorm_runner.backend.post.presentation.post.dto.PostUpdateRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static goorm_runner.backend.global.ErrorCode.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

@SpringBootTest
@Transactional
class PostServiceTest {

    @Autowired
    private PostService postService;

    @Autowired
    private PostRepository postRepository;

    @Test
    void save_success() {
        //given
        String title = "Example title";
        String content = "<h1>Example</h1> Insert content here.";
        PostCreateRequest request = new PostCreateRequest(title, content);

        Long authorId = 1L;

        //when
        Post post = postService.create(title, content, authorId, Category.GENERAL);

        //then
        assertAll(
                () -> assertThat(post.getTitle()).isEqualTo(title),
                () -> assertThat(post.getContent()).isEqualTo(content),
                () -> assertThat(post.getAuthorId()).isEqualTo(authorId),
                () -> assertThat(post.getCategory().name()).isEqualTo(Category.GENERAL.name()),
                () -> assertThat(post.getCreatedAt()).isNotNull(),
                () -> assertThat(post.getUpdatedAt()).isNotNull(),
                () -> assertThat(post.getCreatedAt()).isEqualTo(post.getUpdatedAt())
        );
    }

    @Test
    void save_empty_title_failure() {
        //given
        String title = "";
        String content = "<h1>Example</h1> Insert content here.";
        PostCreateRequest request = new PostCreateRequest(title, content);

        Long authorId = 1L;

        //when-then
        assertThatThrownBy(() -> postService.create(title, content, authorId, Category.GENERAL))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(EMPTY_TITLE.getMessage());
    }

    @Test
    void save_empty_content_failure() {
        //given
        String title = "Example title";
        String content = "";
        PostCreateRequest request = new PostCreateRequest(title, content);

        Long authorId = 1L;

        //when-then
        assertThatThrownBy(() -> postService.create(title, content, authorId, Category.GENERAL))
                .isInstanceOf(PostException.class)
                .hasMessage(EMPTY_CONTENT.getMessage());
    }

    @Test
    void update_success() {
        //given
        String title = "Example title";
        String content = "<h1>Example</h1> Insert content here.";
        PostCreateRequest createRequest = new PostCreateRequest(title, content);

        Long authorId = 1L;

        Post post = postService.create(title, content, authorId, Category.GENERAL);

        //when
        String updatedTitle = "UpdatedTitle";
        String updatedContent = "UpdatedContent";

        Post updated = postService.update(updatedTitle, updatedContent, post.getId());
        postRepository.flush();

        //then
        assertAll(
                () -> assertThat(updated.getTitle()).isEqualTo(updatedTitle),
                () -> assertThat(updated.getContent()).isEqualTo(updatedContent),
                () -> assertThat(updated.getUpdatedAt()).isAfter(updated.getCreatedAt())
        );
    }

    @Test
    void update_with_invalid_postId_failure() {
        //given
        String title = "Example title";
        String content = "<h1>Example</h1> Insert content here.";
        PostCreateRequest createRequest = new PostCreateRequest(title, content);

        Long authorId = 1L;

        Post post = postService.create(title, content, authorId, Category.GENERAL);

        //when
        String updatedTitle = "UpdatedTitle";
        String updatedContent = "UpdatedContent";
        PostUpdateRequest updateRequest = new PostUpdateRequest(updatedTitle, updatedContent);

        //then
        assertThatThrownBy(() -> postService.update(title, content, post.getId() + 1))
                .isInstanceOf(PostException.class)
                .hasMessage(POST_NOT_FOUND.getMessage());
    }

    @Test
    void update_with_empty_title_failure() {
        //given
        String title = "Example title";
        String content = "<h1>Example</h1> Insert content here.";

        Long authorId = 1L;

        Post post = postService.create(title, content, authorId, Category.GENERAL);

        //when
        String emptyTitle = "";
        String updatedContent = "UpdatedContent";

        //then
        assertThatThrownBy(() -> postService.update(emptyTitle, updatedContent, post.getId()))
                .isInstanceOf(PostException.class)
                .hasMessage(EMPTY_TITLE.getMessage());
    }

    @Test
    void update_with_empty_content_failure() {
        //given
        String title = "Example title";
        String content = "<h1>Example</h1> Insert content here.";

        Long authorId = 1L;

        Post post = postService.create(title, content, authorId, Category.GENERAL);

        //when
        String emptyTitle = "UpdatedTitle";
        String updatedContent = "";

        //then
        assertThatThrownBy(() -> postService.update(emptyTitle, updatedContent, post.getId()))
                .isInstanceOf(PostException.class)
                .hasMessage(EMPTY_CONTENT.getMessage());
    }
}