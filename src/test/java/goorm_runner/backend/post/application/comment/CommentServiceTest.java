package goorm_runner.backend.post.application.comment;

import goorm_runner.backend.post.application.comment.dto.CommentCreateResult;
import goorm_runner.backend.post.application.comment.dto.CommentUpdateResult;
import goorm_runner.backend.post.domain.PostRepository;
import goorm_runner.backend.post.domain.exception.CommentException;
import goorm_runner.backend.post.domain.model.Category;
import goorm_runner.backend.post.domain.model.Comment;
import goorm_runner.backend.post.domain.model.Post;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

@Transactional
@SpringBootTest
class CommentServiceTest {

    @Autowired
    private CommentService commentService;

    @Autowired
    private EntityManager em;

    @Autowired
    private PostRepository postRepository;

    @BeforeEach
    void setUp() {
        postRepository.deleteAll();
    }

    @Test
    void save_success() {
        //given
        Long authorId = 1L;
        String content = "lorem ipsum";

        Post post = new Post(1L, "title", "content", Category.GENERAL);
        postRepository.save(post);

        //when
        CommentCreateResult result = commentService.create(authorId, post.getId(), content);

        //then
        assertAll(
                () -> assertThat(result.postId()).isEqualTo(post.getId()),
                () -> assertThat(result.commentId()).isNotNull(),
                () -> assertThat(result.content()).isEqualTo(content),
                () -> assertThat(result.createdAt()).isNotNull()
        );
    }
    
    @Test
    void save_with_empty_content_exception() {
        //given
        Long authorId = 1L;
        String content = "";

        Post post = new Post(authorId, "title", "content", Category.GENERAL);
        postRepository.save(post);

        //when-then
        assertThatThrownBy(() -> commentService.create(authorId, post.getId(), content))
                .isInstanceOf(CommentException.class);
    }

    @Test
    void update_success() {
        //given
        Long authorId = 1L;
        String content = "lorem ipsum";

        Post post = new Post(1L, "title", "content", Category.GENERAL);
        postRepository.save(post);

        CommentCreateResult result = commentService.create(authorId, post.getId(), content);

        //when
        String updatedContent = "updated";
        CommentUpdateResult updateResult = commentService.update(post, result.commentId(), updatedContent);
        em.flush();
        em.clear();

        //then
        assertThat(updateResult.content()).isEqualTo(updatedContent);
    }

    @Test
    void update_with_empty_content_exception() {
        //given
        Long authorId = 1L;
        String content = "lorem ipsum";

        Post post = new Post(authorId, "title", "content", Category.GENERAL);
        postRepository.save(post);

        CommentCreateResult result = commentService.create(authorId, post.getId(), content);

        //when-then
        String emptyContent = "";
        assertThatThrownBy(() -> commentService.update(post, result.commentId(), emptyContent))
                .isInstanceOf(CommentException.class);

    }

}