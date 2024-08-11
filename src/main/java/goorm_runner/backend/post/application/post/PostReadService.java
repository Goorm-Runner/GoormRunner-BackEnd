package goorm_runner.backend.post.application.post;

import goorm_runner.backend.global.ErrorCode;
import goorm_runner.backend.post.application.post.exception.PostException;
import goorm_runner.backend.post.domain.PostQueryRepository;
import goorm_runner.backend.post.domain.model.Category;
import goorm_runner.backend.post.domain.model.Post;
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

    public Page<Post> readPage(Category category, Pageable pageable) {
        return postQueryRepository.findByCategory(category, pageable);
    }

    public boolean existsPost(Long postId) {
        return postQueryRepository.existsById(postId);
    }
}
