package goorm_runner.backend.post.application;

import goorm_runner.backend.post.domain.Post;
import goorm_runner.backend.post.dto.PostCreateRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

@SpringBootTest
class PostServiceTest {

    @Autowired
    private PostService postService;

    @Test
    void save_success() {
        //given
        String title = "Example title";
        String content = "<h1>Example</h1> Insert content here.";
        PostCreateRequest request = new PostCreateRequest(title, content);

        Long authorId = 1L;

        String categoryName = "GENERAL";

        //when
        Post post = postService.create(request, authorId, categoryName);

        //then
        assertAll(
                () -> assertThat(post.getTitle()).isEqualTo(title),
                () -> assertThat(post.getContent()).isEqualTo(content),
                () -> assertThat(post.getAuthorId()).isEqualTo(authorId),
                () -> assertThat(post.getCategory().name()).isEqualTo(categoryName),
                () -> assertThat(post.getLikeCount()).isEqualTo((short) 0),
                () -> assertThat(post.getCreatedAt()).isNotNull(),
                () -> assertThat(post.getUpdatedAt()).isNotNull(),
                () -> assertThat(post.getCreatedAt()).isEqualTo(post.getUpdatedAt())
        );
    }

    @Test
    void empty_title_failure() {
        //given
        String title = "";
        String content = "<h1>Example</h1> Insert content here.";
        PostCreateRequest request = new PostCreateRequest(title, content);

        Long authorId = 1L;

        String categoryName = "GENERAL";

        //when-then
        assertThatThrownBy(() -> postService.create(request, authorId, categoryName))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("제목을 입력해주세요.");
    }

    @Test
    void empty_content_failure() {
        //given
        String title = "Example title";
        String content = "";
        PostCreateRequest request = new PostCreateRequest(title, content);

        Long authorId = 1L;

        String categoryName = "GENERAL";

        //when-then
        assertThatThrownBy(() -> postService.create(request, authorId, categoryName))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("본문 내용을 입력해주세요.");
    }

    @Test
    void invalid_category() {
        //given
        String title = "Example title";
        String content = "<h1>Example</h1> Insert content here.";
        PostCreateRequest request = new PostCreateRequest(title, content);

        Long authorId = 1L;

        String categoryName = "INVALID";

        //when
        assertThatThrownBy(() -> postService.create(request, authorId, categoryName))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("유효하지 않은 카테고리: " + categoryName);
    }
}