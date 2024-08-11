package goorm_runner.backend.post.application.comment;

import goorm_runner.backend.global.ErrorCode;
import goorm_runner.backend.post.application.post.exception.PostException;
import goorm_runner.backend.post.domain.CommentRepository;
import goorm_runner.backend.post.domain.PostRepository;
import goorm_runner.backend.post.domain.exception.CommentException;
import goorm_runner.backend.post.domain.model.Comment;
import goorm_runner.backend.post.domain.model.Post;
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
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostException(ErrorCode.POST_NOT_FOUND));

        Comment comment = post.addComment(authorId, content);
        commentRepository.save(comment);
        return comment;
    }

    public Comment update(Long postId, Long commentId, String content) {
        validateNotEmptyContent(content);

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostException(ErrorCode.POST_NOT_FOUND));

        Comment findComment = post.getComments()
                .stream()
                .filter(comment -> comment.getId().equals(commentId))
                .findFirst()
                .orElseThrow(() -> new CommentException(ErrorCode.COMMENT_NOT_FOUND));

        findComment.updateContent(content);
        return findComment;
    }

    public void delete(Long postId, Long commentId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostException(ErrorCode.POST_NOT_FOUND));

        Comment findComment = post.getComments()
                .stream()
                .filter(comment -> comment.getId().equals(commentId))
                .findFirst()
                .orElseThrow(() -> new CommentException(ErrorCode.COMMENT_NOT_FOUND));

        findComment.delete();
    }

    private void validateNotEmptyContent(String content) {
        if (!StringUtils.hasText(content)) {
            throw new CommentException(ErrorCode.EMPTY_CONTENT);
        }
    }
}
