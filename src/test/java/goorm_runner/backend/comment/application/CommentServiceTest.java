package goorm_runner.backend.comment.application;

import goorm_runner.backend.comment.domain.Comment;
import goorm_runner.backend.comment.domain.exception.CommentException;
import goorm_runner.backend.post.domain.Category;
import goorm_runner.backend.post.domain.Post;
import goorm_runner.backend.post.domain.PostRepository;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@Transactional
@SpringBootTest
class CommentServiceTest {

    @Autowired
    private CommentService commentService;

    @Autowired
    private EntityManager em;

    @MockBean
    private PostRepository postRepository;

    @Test
    void save_success() {
        //given
        Long authorId = 1L;
        Long postId = 1L;
        String content = "lorem ipsum";

        when(postRepository.findById(any()))
                .thenReturn(Optional.of(new Post(1L, "title", "content", Category.GENERAL)));

        //when
        Comment comment = commentService.create(authorId, postId, content);

        //then
        assertAll(
                () -> assertThat(comment.getAuthorId()).isEqualTo(authorId),
                () -> assertThat(comment.getPostId()).isEqualTo(postId),
                () -> assertThat(comment.getContent()).isEqualTo(content),
                () -> assertThat(comment.getCreatedAt()).isNotNull(),
                () -> assertThat(comment.getUpdatedAt()).isNotNull(),
                () -> assertThat(comment.getCreatedAt()).isEqualTo(comment.getUpdatedAt()),
                () -> assertThat(comment.getDeletedAt()).isNull()
        );
    }
    
    @Test
    void save_with_empty_content_exception() {
        //given
        Long authorId = 1L;
        Long postId = 1L;
        String content = "";

        //when
        when(postRepository.findById(any()))
                .thenReturn(Optional.of(new Post(1L, "title", "content", Category.GENERAL)));

        //then
        assertThatThrownBy(() -> commentService.create(authorId, postId, content))
                .isInstanceOf(CommentException.class);
    }

    @Test
    void update_success() {
        //given
        Long authorId = 1L;
        Long postId = 1L;
        String content = "lorem ipsum";

        when(postRepository.findById(any()))
                .thenReturn(Optional.of(new Post(1L, "title", "content", Category.GENERAL)));

        Comment comment = commentService.create(authorId, postId, content);

        //when
        String updatedContent = "updated";
        Comment updatedComment = commentService.update(comment.getId(), updatedContent);
        em.flush();
        em.clear();

        //then
        assertAll(
                () -> assertThat(updatedComment.getContent()).isEqualTo(updatedContent),
                () -> assertThat(updatedComment.getUpdatedAt()).isAfter(updatedComment.getCreatedAt())
        );
    }

    @Test
    void update_with_empty_content_exception() {
        //given
        Long authorId = 1L;
        Long postId = 1L;
        String content = "lorem ipsum";

        when(postRepository.findById(any()))
                .thenReturn(Optional.of(new Post(1L, "title", "content", Category.GENERAL)));

        Comment comment = commentService.create(authorId, postId, content);

        //when-then
        String emptyContent = "";
        assertThatThrownBy(() -> commentService.update(comment.getId(), emptyContent))
                .isInstanceOf(CommentException.class);

    }

}