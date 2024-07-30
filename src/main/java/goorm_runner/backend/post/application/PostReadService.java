package goorm_runner.backend.post.application;

import goorm_runner.backend.post.application.exception.ErrorCode;
import goorm_runner.backend.post.application.exception.PostException;
import goorm_runner.backend.post.domain.Category;
import goorm_runner.backend.post.domain.Post;
import goorm_runner.backend.post.domain.PostQueryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class PostReadService {

    private final PostQueryRepository postQueryRepository;

    public Post readPost(Long postId) {
        return postQueryRepository.findById(postId)
                .orElseThrow(() -> new PostException(ErrorCode.POST_NOT_FOUND));
    }

    public Page<Post> readPage(String categoryName, Pageable pageable) {
        Category category = toCategory(categoryName);
        return postQueryRepository.findByCategory(category, pageable);
    }

    private Category toCategory(String category) {
        try {
            return Category.valueOf(category);
        } catch (IllegalArgumentException e) {
            throw new PostException(ErrorCode.INVALID_CATEGORY);
        }
    }
}
