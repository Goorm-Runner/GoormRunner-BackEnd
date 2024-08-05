package goorm_runner.backend.postlike.application;

import goorm_runner.backend.postlike.domain.PostLike;
import goorm_runner.backend.postlike.domain.PostLikeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class PostLikeService {

    private final PostLikeRepository postLikeRepository;

    public void likePost(Long postId, Long memberId) {
        PostLike postLike = new PostLike(postId, memberId);
        postLikeRepository.save(postLike);
    }

    public int countPostLikes(Long postId) {
        return postLikeRepository.countByPostId(postId);
    }
}
