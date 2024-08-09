package goorm_runner.backend.comment.application;

import goorm_runner.backend.comment.domain.Comment;
import goorm_runner.backend.member.domain.Member;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@Transactional
@SpringBootTest
class CommentServiceTest {

    @Autowired
    private CommentService commentService;

    @Test
    void save_success() {
        //given
        Long authorId = 1L;
        Long postId = 1L;
        String content = "lorem ipsum";

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
}