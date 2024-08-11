package goorm_runner.backend.post.application.comment;

import goorm_runner.backend.global.ErrorCode;
import goorm_runner.backend.post.domain.CommentRepository;
import goorm_runner.backend.post.domain.exception.CommentException;
import goorm_runner.backend.post.domain.model.Comment;
import goorm_runner.backend.post.domain.model.Post;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class CommentService {

    private final CommentRepository commentRepository;

    public Comment create(Long authorId, Post post, String content) {
        Comment comment = post.addComment(authorId, content);
        commentRepository.save(comment);
        return comment;
    }

    public Comment update(Post post, Long commentId, String content) {
        Comment findComment = post.getComments()
                .stream()
                .filter(comment -> comment.getId().equals(commentId))
                .findFirst()
                .orElseThrow(() -> new CommentException(ErrorCode.COMMENT_NOT_FOUND));

        findComment.updateContent(content);
        return findComment;
    }

    public void delete(Post post, Long commentId) {
        Comment findComment = post.getComments()
                .stream()
                .filter(comment -> comment.getId().equals(commentId))
                .findFirst()
                .orElseThrow(() -> new CommentException(ErrorCode.COMMENT_NOT_FOUND));

        findComment.delete();
    }
}
