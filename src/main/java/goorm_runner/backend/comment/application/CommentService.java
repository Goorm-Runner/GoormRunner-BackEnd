package goorm_runner.backend.comment.application;

import goorm_runner.backend.comment.domain.Comment;
import goorm_runner.backend.comment.domain.CommentRepository;
import goorm_runner.backend.comment.domain.exception.CommentException;
import goorm_runner.backend.global.ErrorCode;
import goorm_runner.backend.post.application.exception.PostException;
import goorm_runner.backend.post.domain.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Service
@RequiredArgsConstructor
@Transactional
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;

    public Comment create(Long authorId, Long postId, String content) {
        postRepository.findById(postId)
                .orElseThrow(() -> new PostException(ErrorCode.POST_NOT_FOUND));

        Comment comment = new Comment(authorId, postId, content);
        return commentRepository.save(comment);
    }

    public Comment update(Long commentId, String content) {
        validateNotEmptyContent(content);
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new CommentException(ErrorCode.COMMENT_NOT_FOUND));

        comment.updateContent(content);
        return comment;
    }

    private void validateNotEmptyContent(String content) {
        if (!StringUtils.hasText(content)) {
            throw new CommentException(ErrorCode.EMPTY_CONTENT);
        }
    }

    public void delete(Long commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new CommentException(ErrorCode.COMMENT_NOT_FOUND));

        comment.delete();
    }
}
