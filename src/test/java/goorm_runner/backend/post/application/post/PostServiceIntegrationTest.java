package goorm_runner.backend.post.application.post;

import goorm_runner.backend.global.ErrorCode;
import goorm_runner.backend.member.domain.Member;
import goorm_runner.backend.member.domain.MemberRepository;
import goorm_runner.backend.post.application.post.dto.PostReadPageResult;
import goorm_runner.backend.post.domain.PostRepository;
import goorm_runner.backend.post.domain.exception.PostException;
import goorm_runner.backend.post.domain.model.Category;
import goorm_runner.backend.post.domain.model.Post;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
@Transactional
public class PostServiceIntegrationTest {

    @Autowired
    private PostService postService;

    @Autowired
    private PostReadService postReadService;

    @Autowired
    private PostRepository postRepository;

    @MockBean
    private MemberRepository memberRepository;

    @BeforeEach
    void setUp() {
        postRepository.deleteAll();
    }

    @Test
    void delete_success() {
        //given
        String title = "Example title";
        String content = "<h1>Example</h1> Insert content here.";

        Long authorId = 1L;

        Post post = postService.create(title, content, authorId, Category.GENERAL);
        Long postId = post.getId();

        Member mockMember = mock(Member.class);
        when(mockMember.getId()).thenReturn(authorId);

        when(memberRepository.findById(any()))
                .thenReturn(Optional.of(mockMember));

        //when
        postService.delete(postId, authorId);

        //then
        assertThat(post.getDeletedAt()).isNotNull();
    }

    @Test
    void delete_failure() {
        //given
        String title = "Example title";
        String content = "<h1>Example</h1> Insert content here.";

        Long authorId = 1L;

        Post post = postService.create(title, content, authorId, Category.GENERAL);
        Long postId = post.getId();

        //then
        assertThatThrownBy(() -> postService.delete(postId + 1, authorId))
                .isInstanceOf(PostException.class)
                .hasMessage(ErrorCode.POST_NOT_FOUND.getMessage());
    }

    @Test
    void delete_then_cannot_read_page() {
        //given
        String title1 = "Example title";
        String title2 = "Example title2";
        String content = "<h1>Example</h1> Insert content here.";

        Long authorId = 1L;

        Member mockMember = mock(Member.class);
        when(mockMember.getId()).thenReturn(authorId);

        when(memberRepository.findById(any()))
                .thenReturn(Optional.of(mockMember));

        Post post1 = postService.create(title1, content, authorId, Category.GENERAL);
        postService.create(title2, content, authorId, Category.GENERAL);

        //when
        postService.delete(post1.getId(), authorId);
        PageRequest pageRequest = PageRequest.of(0, 10);
        PostReadPageResult result = postReadService.readPage(Category.GENERAL, pageRequest);

        //then
        assertThat(result.overviews().size()).isEqualTo(1);
        assertThat(result.overviews().get(0).title()).isEqualTo(title2);
    }
}
