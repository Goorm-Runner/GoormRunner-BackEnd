package goorm_runner.backend.post.application.comment;

import goorm_runner.backend.global.ErrorCode;
import goorm_runner.backend.post.domain.CommentQueryRepository;
import goorm_runner.backend.post.domain.PostRepository;
import goorm_runner.backend.post.domain.exception.CommentException;
import goorm_runner.backend.post.domain.model.Comment;
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
    public Comment read(Long commentId) {
        return commentQueryRepository.findById(commentId)
                .orElseThrow(() -> new CommentException(ErrorCode.COMMENT_NOT_FOUND));
    }

    @Transactional(readOnly = true)
    public Page<Comment> readPage(Long postId, Pageable pageable) {
        return commentQueryRepository.findByPostId(postId, pageable);
    }
}
