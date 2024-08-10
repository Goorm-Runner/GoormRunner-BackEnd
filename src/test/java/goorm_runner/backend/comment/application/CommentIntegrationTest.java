package goorm_runner.backend.comment.application;

import goorm_runner.backend.post.application.comment.CommentReadService;
import goorm_runner.backend.post.application.comment.CommentService;
import goorm_runner.backend.post.domain.comment.Comment;
import goorm_runner.backend.post.domain.comment.exception.CommentException;
import goorm_runner.backend.post.domain.post.Category;
import goorm_runner.backend.post.domain.post.Post;
import goorm_runner.backend.post.domain.post.PostRepository;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@Transactional
@SpringBootTest
public class CommentIntegrationTest {

    @Autowired
    private CommentService commentService;

    @Autowired
    private CommentReadService commentReadService;

    @Autowired
    private EntityManager em;

    @MockBean
    private PostRepository postRepository;

    @Test
    void delete_success() {
        //given
        Long authorId = 1L;
        Long postId = 1L;
        String content = "lorem ipsum";

        when(postRepository.findById(any()))
                .thenReturn(Optional.of(new Post(1L, "title", "content", Category.GENERAL)));

        Comment comment = commentService.create(authorId, postId, content);

        //when
        commentService.delete(comment.getId());
        em.flush();
        em.clear();

        //then
        assertThatThrownBy(() -> commentReadService.read(postId, comment.getId()))
                .isInstanceOf(CommentException.class);
    }
}
