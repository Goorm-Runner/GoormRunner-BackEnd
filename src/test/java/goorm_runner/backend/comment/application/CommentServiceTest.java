package goorm_runner.backend.comment.application;

import goorm_runner.backend.post.application.comment.CommentService;
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
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.util.ReflectionTestUtils;
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

    @BeforeEach
    void setUp() {
        postRepository.deleteAll();
    }

    @Test
    void save_success() {
        //given
        Long authorId = 1L;
        Long postId = 1L;
        String content = "lorem ipsum";

        Post post = new Post(1L, "title", "content", Category.GENERAL);
        ReflectionTestUtils.setField(post, "id", 1L);
        when(postRepository.findById(any())).thenReturn(Optional.of(post));

        //when
        Comment comment = commentService.create(authorId, postId, content);

        //then
        assertAll(
                () -> assertThat(comment.getAuthorId()).isEqualTo(authorId),
                () -> assertThat(comment.getPost().getId()).isEqualTo(postId),
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

        Post post = new Post(1L, "title", "content", Category.GENERAL);
        ReflectionTestUtils.setField(post, "id", postId);
        when(postRepository.findById(any())).thenReturn(Optional.of(post));

        Comment comment = commentService.create(authorId, postId, content);

        //when
        String updatedContent = "updated";
        Comment updatedComment = commentService.update(postId, comment.getId(), updatedContent);
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

        Post post = new Post(1L, "title", "content", Category.GENERAL);
        ReflectionTestUtils.setField(post, "id", postId);
        when(postRepository.findById(any())).thenReturn(Optional.of(post));

        Comment comment = commentService.create(authorId, postId, content);

        //when-then
        String emptyContent = "";
        assertThatThrownBy(() -> commentService.update(postId, comment.getId(), emptyContent))
                .isInstanceOf(CommentException.class);

    }

}