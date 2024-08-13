package goorm_runner.backend.post.application.comment;

import goorm_runner.backend.global.PageMetaData;
import goorm_runner.backend.member.domain.Member;
import goorm_runner.backend.member.domain.MemberRepository;
import goorm_runner.backend.member.domain.Role;
import goorm_runner.backend.member.domain.Sex;
import goorm_runner.backend.post.application.comment.dto.CommentCreateResult;
import goorm_runner.backend.post.application.comment.dto.CommentPageResult;
import goorm_runner.backend.post.application.comment.dto.CommentReadResult;
import goorm_runner.backend.post.application.comment.dto.CommentResultOverview;
import goorm_runner.backend.post.domain.PostRepository;
import goorm_runner.backend.post.domain.model.Category;
import goorm_runner.backend.post.domain.model.Post;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@Transactional
@SpringBootTest
class CommentReadServiceTest {

    @Autowired
    private CommentService commentService;

    @Autowired
    private CommentReadService commentReadService;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Test
    void read_success() {
        //given
        String loginId = "loginId";
        String nickname = "username";
        String password = "password";

        Member member = memberRepository.save(
                Member.builder()
                        .loginId(loginId)
                        .nickname(nickname)
                        .password(password)
                        .role(Role.USER)
                        .sex(Sex.MALE)
                        .birth(LocalDate.now())
                        .build()
        );

        memberRepository.save(member);

        Post post = new Post(member.getId(), "title", "content", Category.GENERAL);
        postRepository.save(post);
        postRepository.flush();

        CommentCreateResult result = commentService.create(member.getId(), post.getId(), "content");

        //when
        CommentReadResult commentReadResult = commentReadService.read(result.commentId());

        //then
        assertAll(
                () -> assertThat(commentReadResult.commentId()).isEqualTo(result.commentId()),
                () -> assertThat(commentReadResult.content()).isEqualTo(result.content())
        );
    }

    @Test
    void read_page_success() {
        //given
        String loginId = "loginId";
        String nickname = "username";
        String password = "password";

        Member member = memberRepository.save(
                Member.builder()
                        .loginId(loginId)
                        .nickname(nickname)
                        .password(password)
                        .role(Role.USER)
                        .sex(Sex.MALE)
                        .birth(LocalDate.now())
                        .build()
        );

        Long authorId = member.getId();
        String content = "lorem ipsum";

        Post post = new Post(member.getId(), "title", "content", Category.GENERAL);
        postRepository.save(post);

        commentService.create(authorId, post.getId(), content);

        //when
        PageRequest pageRequest = PageRequest.of(0, 10);
        CommentPageResult commentPageResult = commentReadService.readPage(post.getId(), pageRequest);

        //then
        List<CommentResultOverview> overviews = commentPageResult.overviews();
        PageMetaData pageMetaData = commentPageResult.pageMetaData();
        assertAll(
                () -> assertThat(overviews.size()).isEqualTo(1),
                () -> assertThat(pageMetaData.number()).isEqualTo(0),
                () -> assertThat(pageMetaData.size()).isEqualTo(10),
                () -> assertThat(pageMetaData.isFirst()).isTrue(),
                () -> assertThat(pageMetaData.isLast()).isTrue(),
                () -> assertThat(pageMetaData.hasPrevious()).isFalse(),
                () -> assertThat(pageMetaData.hasNext()).isFalse()
        );
    }

}