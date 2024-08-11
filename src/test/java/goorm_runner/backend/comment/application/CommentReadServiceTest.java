package goorm_runner.backend.comment.application;

import goorm_runner.backend.post.application.comment.CommentReadService;
import goorm_runner.backend.post.application.comment.CommentService;
import goorm_runner.backend.post.domain.CommentRepository;
import goorm_runner.backend.post.domain.PostRepository;
import goorm_runner.backend.post.domain.model.Category;
import goorm_runner.backend.post.domain.model.Comment;
import goorm_runner.backend.post.domain.model.Post;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
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

    @Autowired
    private CommentRepository commentRepository;

    @MockBean
    private PostRepository postRepository;


    @BeforeEach
    void setUp() {
        commentRepository.deleteAll();
    }

    @Test
    void read_success() {
        //given
        Long authorId = 1L;
        Long postId = 1L;
        String content = "lorem ipsum";

        Post post = new Post(1L, "title", "content", Category.GENERAL);
        ReflectionTestUtils.setField(post, "id", 1L);
        when(postRepository.findById(any()))
                .thenReturn(Optional.of(post));

        Comment comment = commentService.create(authorId, postId, content);

        //when
        Comment findComment = commentReadService.read(comment.getId());

        //then
        assertAll(
                () -> assertThat(findComment.getId()).isEqualTo(comment.getId()),
                () -> assertThat(findComment.getPost().getId()).isEqualTo(comment.getPost().getId()),
                () -> assertThat(findComment.getAuthorId()).isEqualTo(comment.getAuthorId()),
                () -> assertThat(findComment.getContent()).isEqualTo(comment.getContent()),
                () -> assertThat(findComment.getCreatedAt()).isEqualTo(comment.getCreatedAt()),
                () -> assertThat(findComment.getUpdatedAt()).isEqualTo(comment.getUpdatedAt()),
                () -> assertThat(findComment.getDeletedAt()).isEqualTo(comment.getDeletedAt())
        );
    }

    @Test
    void read_page_success() {
        //given
        Long authorId = 1L;
        Long postId = 1L;
        String content = "lorem ipsum";

        Post post = new Post(1L, "title", "content", Category.GENERAL);
        ReflectionTestUtils.setField(post, "id", 1L);
        when(postRepository.findById(any()))
                .thenReturn(Optional.of(post));

        commentService.create(authorId, postId, content);

        //when
        PageRequest pageRequest = PageRequest.of(0, 10);
        Page<Comment> page = commentReadService.readPage(postId, pageRequest);

        //then
        List<Comment> contents = page.getContent();
        assertAll(
                () -> assertThat(contents.size()).isEqualTo(1),
                () -> assertThat(page.getTotalPages()).isEqualTo(1),
                () -> assertThat(page.getNumber()).isEqualTo(0),
                () -> assertThat(page.getTotalPages()).isEqualTo(1),
                () -> assertThat(page.isFirst()).isTrue(),
                () -> assertThat(page.hasNext()).isFalse()
        );
    }

}