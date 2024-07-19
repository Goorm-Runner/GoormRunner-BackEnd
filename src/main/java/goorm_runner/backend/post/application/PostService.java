package goorm_runner.backend.post.application;

import goorm_runner.backend.post.domain.Category;
import goorm_runner.backend.post.domain.Post;
import goorm_runner.backend.post.domain.PostRepository;
import goorm_runner.backend.post.dto.PostCreateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@RequiredArgsConstructor
@Service
@Transactional
public class PostService {

    private final PostRepository postRepository;

    public Post create(PostCreateRequest request, Long authorId, String categoryName) {
        validateTitleAndContent(request);

        Category category = toCategory(categoryName);
        Post post = getPost(request, authorId, category);

        return postRepository.save(post);
    }

    private void validateTitleAndContent(PostCreateRequest request) {
        if (!StringUtils.hasText(request.getTitle())) {
            throw new IllegalArgumentException("제목을 입력해주세요.");
        }

        if (!StringUtils.hasText(request.getContent())) {
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
