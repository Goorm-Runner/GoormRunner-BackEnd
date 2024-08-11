package goorm_runner.backend.comment.application;

import goorm_runner.backend.post.application.comment.CommentReadService;
import goorm_runner.backend.post.application.comment.CommentService;
import goorm_runner.backend.post.domain.PostRepository;
import goorm_runner.backend.post.domain.exception.CommentException;
import goorm_runner.backend.post.domain.model.Category;
import goorm_runner.backend.post.domain.model.Comment;
import goorm_runner.backend.post.domain.model.Post;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.util.ReflectionTestUtils;
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

        Post post = new Post(1L, "title", "content", Category.GENERAL);
        ReflectionTestUtils.setField(post, "id", 1L);
        when(postRepository.findById(any()))
                .thenReturn(Optional.of(post));

        Comment comment = commentService.create(authorId, post, content);

        //when
        commentService.delete(post, comment.getId());
        em.flush();
        em.clear();

        //then
        assertThatThrownBy(() -> commentReadService.read(comment.getId()))
                .isInstanceOf(CommentException.class);
    }
}
