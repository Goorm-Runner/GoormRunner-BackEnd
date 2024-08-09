package goorm_runner.backend.comment.application;

import goorm_runner.backend.comment.domain.Comment;
import goorm_runner.backend.comment.domain.CommentReadRepository;
import goorm_runner.backend.comment.domain.exception.CommentException;
import goorm_runner.backend.global.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommentReadService {

    private final CommentReadRepository commentReadRepository;

    public Comment read(Long commentId) {
        return commentReadRepository.findById(commentId)
                .orElseThrow(() -> new CommentException(ErrorCode.COMMENT_NOT_FOUND));
    }
}
