package goorm_runner.backend.comment.application;

import goorm_runner.backend.comment.domain.Comment;
import goorm_runner.backend.post.domain.Category;
import goorm_runner.backend.post.domain.Post;
import goorm_runner.backend.post.domain.PostRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@Transactional
@SpringBootTest
class CommentReadServiceTest {

    @Autowired
    private CommentService commentService;

    @Autowired
    private CommentReadService commentReadService;

    @MockBean
    private PostRepository postRepository;

    @Test
    void read_success() {
        //given
        Long authorId = 1L;
        Long postId = 1L;
        String content = "lorem ipsum";

        when(postRepository.findById(any()))
                .thenReturn(Optional.of(new Post(1L, "title", "content", Category.GENERAL)));

        Comment comment = commentService.create(authorId, postId, content);

        //when
        Comment findComment = commentReadService.read(comment.getId());

        //then
        assertAll(
                () -> assertThat(findComment.getId()).isEqualTo(comment.getId()),
                () -> assertThat(findComment.getPostId()).isEqualTo(comment.getPostId()),
                () -> assertThat(findComment.getAuthorId()).isEqualTo(comment.getAuthorId()),
                () -> assertThat(findComment.getContent()).isEqualTo(comment.getContent()),
                () -> assertThat(findComment.getCreatedAt()).isEqualTo(comment.getCreatedAt()),
                () -> assertThat(findComment.getUpdatedAt()).isEqualTo(comment.getUpdatedAt()),
                () -> assertThat(findComment.getDeletedAt()).isEqualTo(comment.getDeletedAt())
        );
    }

}