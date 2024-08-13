package goorm_runner.backend.post.application.post;

import goorm_runner.backend.global.PageMetaData;
import goorm_runner.backend.member.domain.Member;
import goorm_runner.backend.member.domain.MemberRepository;
import goorm_runner.backend.member.domain.Role;
import goorm_runner.backend.member.domain.Sex;
import goorm_runner.backend.post.application.post.dto.PostReadPageResult;
import goorm_runner.backend.post.domain.model.Category;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@SpringBootTest
@Transactional
class PostReadServiceTest {

    @Autowired
    private PostReadService postReadService;

    @Autowired
    private PostService postService;

    @Autowired
    private MemberRepository memberRepository;


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

        String title = "Example title";
        String content = "<h1>Example</h1> Insert content here.";

        postService.create(title, content, member.getId(), Category.GENERAL);

        //when
        PageRequest pageRequest = PageRequest.of(0, 10);
        PostReadPageResult result = postReadService.readPage(Category.GENERAL, pageRequest);
        PageMetaData pageMetaData = result.pageMetaData();

        //then
        assertAll(
                () -> assertThat(result.overviews().size()).isEqualTo(1),
                () -> assertThat(pageMetaData.number()).isEqualTo(0),
                () -> assertThat(pageMetaData.size()).isEqualTo(10),
                () -> assertThat(pageMetaData.isFirst()).isTrue(),
                () -> assertThat(pageMetaData.isLast()).isTrue(),
                () -> assertThat(pageMetaData.hasPrevious()).isFalse(),
                () -> assertThat(pageMetaData.hasNext()).isFalse()
        );
    }
}