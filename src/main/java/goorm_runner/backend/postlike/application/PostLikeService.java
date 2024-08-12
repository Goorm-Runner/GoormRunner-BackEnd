package goorm_runner.backend.postlike.application;

import goorm_runner.backend.global.ErrorCode;
import goorm_runner.backend.postlike.application.exception.PostLikeException;
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
        validateNotAlreadyLiked(postId, memberId);

        PostLike postLike = new PostLike(postId, memberId);
        postLikeRepository.save(postLike);
    }

    public void deletePostLike(Long postId, Long memberId) {
        validateAlreadyLiked(postId, memberId);
        postLikeRepository.deleteByPostIdAndMemberId(postId, memberId);
    }

    public int countPostLikes(Long postId) {
        return postLikeRepository.countByPostId(postId);
    }

    private void validateNotAlreadyLiked(Long postId, Long memberId) {
        if (postLikeRepository.existsByPostIdAndMemberId(postId, memberId)) {
            throw new PostLikeException(ErrorCode.ALREADY_LIKED);
        }
    }

    private void validateAlreadyLiked(Long postId, Long memberId) {
        if (!postLikeRepository.existsByPostIdAndMemberId(postId, memberId)) {
            throw new PostLikeException(ErrorCode.NOT_ALREADY_LIKED);
        }
    }
}
