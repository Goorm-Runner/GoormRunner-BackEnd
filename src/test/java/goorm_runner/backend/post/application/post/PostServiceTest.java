package goorm_runner.backend.post.application.post;

import goorm_runner.backend.post.application.post.dto.PostCreateResult;
import goorm_runner.backend.post.application.post.dto.PostUpdateResult;
import goorm_runner.backend.post.domain.PostRepository;
import goorm_runner.backend.post.domain.exception.PostException;
import goorm_runner.backend.post.domain.model.Category;
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

        Long authorId = 1L;

        //when
        PostCreateResult result = postService.create(title, content, authorId, Category.GENERAL);

        //then
        assertAll(
                () -> assertThat(result.title()).isEqualTo(title),
                () -> assertThat(result.content()).isEqualTo(content),
                () -> assertThat(result.categoryName()).isEqualTo(Category.GENERAL.name()),
                () -> assertThat(result.createdAt()).isNotNull()
        );
    }

    @Test
    void save_empty_title_failure() {
        //given
        String title = "";
        String content = "<h1>Example</h1> Insert content here.";

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

        Long authorId = 1L;

        PostCreateResult result = postService.create(title, content, authorId, Category.GENERAL);

        //when
        String updatedTitle = "UpdatedTitle";
        String updatedContent = "UpdatedContent";

        PostUpdateResult updateResult = postService.update(updatedTitle, updatedContent, result.postId(), authorId);
        postRepository.flush();

        //then
        assertAll(
                () -> assertThat(updateResult.title()).isEqualTo(updatedTitle),
                () -> assertThat(updateResult.content()).isEqualTo(updatedContent)
        );
    }

    @Test
    void update_with_invalid_postId_failure() {
        //given
        String title = "Example title";
        String content = "<h1>Example</h1> Insert content here.";

        Long authorId = 1L;

        PostCreateResult result = postService.create(title, content, authorId, Category.GENERAL);

        //when
        String updatedTitle = "UpdatedTitle";
        String updatedContent = "UpdatedContent";

        //then
        assertThatThrownBy(() -> postService.update(updatedTitle, updatedContent, result.postId() + 1, authorId))
                .isInstanceOf(PostException.class)
                .hasMessage(POST_NOT_FOUND.getMessage());
    }

    @Test
    void update_with_empty_title_failure() {
        //given
        String title = "Example title";
        String content = "<h1>Example</h1> Insert content here.";

        Long authorId = 1L;

        PostCreateResult result = postService.create(title, content, authorId, Category.GENERAL);

        //when
        String emptyTitle = "";
        String updatedContent = "UpdatedContent";

        //then
        assertThatThrownBy(() -> postService.update(emptyTitle, updatedContent, result.postId(), authorId))
                .isInstanceOf(PostException.class)
                .hasMessage(EMPTY_TITLE.getMessage());
    }

    @Test
    void update_with_empty_content_failure() {
        //given
        String title = "Example title";
        String content = "<h1>Example</h1> Insert content here.";

        Long authorId = 1L;

        PostCreateResult result = postService.create(title, content, authorId, Category.GENERAL);

        //when
        String emptyTitle = "UpdatedTitle";
        String updatedContent = "";

        //then
        assertThatThrownBy(() -> postService.update(emptyTitle, updatedContent, result.postId(), authorId))
                .isInstanceOf(PostException.class)
                .hasMessage(EMPTY_CONTENT.getMessage());
    }
}