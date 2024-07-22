package goorm_runner.backend.post.application;

import goorm_runner.backend.post.domain.Post;
import goorm_runner.backend.post.domain.PostRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class PostReadService {

    private final PostRepository postRepository;

    public Post readPost(Long postId) {
        return postRepository.findById(postId)
                .orElseThrow(() -> new EntityNotFoundException("게시글을 찾을 수 없습니다."));
    }
}
