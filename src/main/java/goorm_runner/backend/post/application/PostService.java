package goorm_runner.backend.post.application;

import goorm_runner.backend.member.application.exception.MemberException;
import goorm_runner.backend.member.domain.Member;
import goorm_runner.backend.member.domain.MemberRepository;
import goorm_runner.backend.post.application.exception.PostException;
import goorm_runner.backend.post.domain.Category;
import goorm_runner.backend.post.domain.Post;
import goorm_runner.backend.post.domain.PostRepository;
import goorm_runner.backend.post.dto.PostCreateRequest;
import goorm_runner.backend.post.dto.PostUpdateRequest;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import static goorm_runner.backend.global.ErrorCode.*;

@RequiredArgsConstructor
@Service
@Transactional
public class PostService {

    private final PostRepository postRepository;
    private final MemberRepository memberRepository;

    public Post create(PostCreateRequest request, Long authorId, String categoryName) {
        validateTitleAndContent(request.title(), request.content());

        Category category = toCategory(categoryName);
        Post post = getPost(request, authorId, category);

        return postRepository.save(post);
    }

    public Post update(PostUpdateRequest request, Long postId) {
        validateTitleAndContent(request.title(), request.content());

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostException(POST_NOT_FOUND));

        post.update(request.title(), request.content());
        return post;
    }

    public void delete(Long postId, Long authorId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostException(POST_NOT_FOUND));

        Member author = memberRepository.findById(authorId)
                .orElseThrow(() -> new MemberException(MEMBER_NOT_FOUND));

        if (!post.getAuthorId().equals(author.getId())) {
            throw new PostException(NOT_POST_AUTHOR);
        }

        post.delete();
    }

    public String getAuthorName(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostException(POST_NOT_FOUND));

        Long authorId = post.getAuthorId();
        Member author = memberRepository.findById(authorId)
                .orElseThrow(() -> new MemberException(MEMBER_NOT_FOUND));

        return author.getNickname();
    }

    private void validateTitleAndContent(String title, String content) {
        if (!StringUtils.hasText(title)) {
            throw new PostException(EMPTY_TITLE);
        }

        if (!StringUtils.hasText(content)) {
            throw new PostException(EMPTY_CONTENT);
        }
    }

    private Category toCategory(String category) {
        try {
            return Category.valueOf(category);
        } catch (IllegalArgumentException e) {
            throw new PostException(INVALID_CATEGORY);
        }
    }

    private Post getPost(PostCreateRequest request, Long authorId, Category category) {
        return Post.builder()
                .authorId(authorId)
                .title(request.title())
                .content(request.content())
                .category(category)
                .build();
    }
}
