package goorm_runner.backend.comment.application;

import goorm_runner.backend.comment.domain.Comment;
import goorm_runner.backend.comment.domain.CommentReadRepository;
import goorm_runner.backend.comment.domain.exception.CommentException;
import goorm_runner.backend.global.ErrorCode;
import goorm_runner.backend.post.domain.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommentReadService {

    private final PostRepository postRepository;
    private final CommentReadRepository commentReadRepository;

    public Comment read(Long postId, Long commentId) {
        validatePostExisting(postId);
        return commentReadRepository.findById(commentId)
                .orElseThrow(() -> new CommentException(ErrorCode.COMMENT_NOT_FOUND));
    }

    private void validatePostExisting(Long postId) {
        postRepository.findById(postId)
                .orElseThrow(() -> new CommentException(ErrorCode.POST_NOT_FOUND));
    }
}
