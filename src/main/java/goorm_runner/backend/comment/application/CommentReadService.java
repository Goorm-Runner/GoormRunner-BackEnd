package goorm_runner.backend.comment.application;

import goorm_runner.backend.comment.domain.Comment;
import goorm_runner.backend.comment.domain.CommentQueryRepository;
import goorm_runner.backend.comment.domain.exception.CommentException;
import goorm_runner.backend.global.ErrorCode;
import goorm_runner.backend.post.domain.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommentReadService {

    private final PostRepository postRepository;
    private final CommentQueryRepository commentQueryRepository;

    public Comment read(Long postId, Long commentId) {
        validatePostExisting(postId);
        return commentQueryRepository.findById(commentId)
                .orElseThrow(() -> new CommentException(ErrorCode.COMMENT_NOT_FOUND));
    }

    private void validatePostExisting(Long postId) {
        postRepository.findById(postId)
                .orElseThrow(() -> new CommentException(ErrorCode.POST_NOT_FOUND));
    }

    public Page<Comment> readPage(Long postId, Pageable pageable) {
        return commentQueryRepository.findByPostId(postId, pageable);
    }
}
