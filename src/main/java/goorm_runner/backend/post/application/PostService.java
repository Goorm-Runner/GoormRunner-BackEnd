package goorm_runner.backend.post.application;

import goorm_runner.backend.member.application.MemberRepository;
import goorm_runner.backend.member.domain.Member;
import goorm_runner.backend.post.domain.Category;
import goorm_runner.backend.post.domain.Post;
import goorm_runner.backend.post.domain.PostRepository;
import goorm_runner.backend.post.dto.PostCreateRequest;
import goorm_runner.backend.post.dto.PostUpdateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@RequiredArgsConstructor
@Service
@Transactional
public class PostService {

    private final PostRepository postRepository;
    private final MemberRepository memberRepository;

    public Post create(PostCreateRequest request, Long authorId, String categoryName) {
        validateTitleAndContent(request.getTitle(), request.getContent());

        Category category = toCategory(categoryName);
        Post post = getPost(request, authorId, category);

        return postRepository.save(post);
    }

    public Post update(PostUpdateRequest request, Long postId) {
        validateTitleAndContent(request.getTitle(), request.getContent());

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("Post not found"));

        post.update(request.getTitle(), request.getContent());
        return post;
    }

    public void delete(Long postId) {
        postRepository.deleteById(postId);
    }

    public String getAuthorName(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("Post not found"));

        Long authorId = post.getAuthorId();
        Member author = memberRepository.findById(authorId)
                .orElseThrow(() -> new IllegalArgumentException("Member not found"));

        return author.getUsername();
    }

    private void validateTitleAndContent(String title, String content) {
        if (!StringUtils.hasText(title)) {
            throw new IllegalArgumentException("제목을 입력해주세요.");
        }

        if (!StringUtils.hasText(content)) {
            throw new IllegalArgumentException("본문 내용을 입력해주세요.");
        }
    }

    private Category toCategory(String category) {
        try {
            return Category.valueOf(category);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("유효하지 않은 카테고리: " + category);
        }
    }

    private Post getPost(PostCreateRequest request, Long authorId, Category category) {
        return Post.builder()
                .authorId(authorId)
                .title(request.getTitle())
                .content(request.getContent())
                .likeCount((short) 0)
                .category(category)
                .build();
    }
}
