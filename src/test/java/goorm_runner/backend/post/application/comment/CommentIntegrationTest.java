package goorm_runner.backend.post.application.comment;

import goorm_runner.backend.post.application.comment.dto.CommentCreateResult;
import goorm_runner.backend.post.domain.PostRepository;
import goorm_runner.backend.post.domain.exception.CommentException;
import goorm_runner.backend.post.domain.model.Category;
import goorm_runner.backend.post.domain.model.Post;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Transactional
@SpringBootTest
public class CommentIntegrationTest {

    @Autowired
    private CommentService commentService;

    @Autowired
    private CommentReadService commentReadService;

    @Autowired
    private EntityManager em;

    @Autowired
    private PostRepository postRepository;

    @Test
    void delete_success() {
        //given
        Long authorId = 1L;
        String content = "lorem ipsum";

        Post post = new Post(1L, "title", "content", Category.GENERAL);
        postRepository.save(post);

        CommentCreateResult result = commentService.create(authorId, post.getId(), content);

        //when
        commentService.delete(post, result.commentId());
        em.flush();
        em.clear();

        //then
        assertThatThrownBy(() -> commentReadService.read(result.commentId()))
                .isInstanceOf(CommentException.class);
    }
}
