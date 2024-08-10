package goorm_runner.backend.post.application.comment;

import goorm_runner.backend.global.ErrorCode;
import goorm_runner.backend.post.domain.comment.Comment;
import goorm_runner.backend.post.domain.comment.CommentQueryRepository;
import goorm_runner.backend.post.domain.comment.exception.CommentException;
import goorm_runner.backend.post.domain.post.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CommentReadService {

    private final PostRepository postRepository;
    private final CommentQueryRepository commentQueryRepository;

    @Transactional(readOnly = true)
    public Comment read(Long postId, Long commentId) {
        validatePostExisting(postId);
        return commentQueryRepository.findById(commentId)
                .orElseThrow(() -> new CommentException(ErrorCode.COMMENT_NOT_FOUND));
    }

    @Transactional(readOnly = true)
    public Page<Comment> readPage(Long postId, Pageable pageable) {
        return commentQueryRepository.findByPostId(postId, pageable);
    }

    private void validatePostExisting(Long postId) {
        postRepository.findById(postId)
                .orElseThrow(() -> new CommentException(ErrorCode.POST_NOT_FOUND));
    }
}
