package goorm_runner.backend.post.application.comment;

import goorm_runner.backend.global.ErrorCode;
import goorm_runner.backend.post.application.comment.dto.CommentCreateResult;
import goorm_runner.backend.post.application.comment.dto.CommentUpdateResult;
import goorm_runner.backend.post.domain.CommentRepository;
import goorm_runner.backend.post.domain.PostRepository;
import goorm_runner.backend.post.domain.exception.CommentException;
import goorm_runner.backend.post.domain.exception.PostException;
import goorm_runner.backend.post.domain.model.Comment;
import goorm_runner.backend.post.domain.model.Post;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Service
@RequiredArgsConstructor
@Transactional
public class CommentService {

    private final PostRepository postRepository;
    private final CommentRepository commentRepository;

    public CommentCreateResult create(Long authorId, Long postId, String content) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostException(ErrorCode.POST_NOT_FOUND));

        Comment comment = new Comment(post, authorId, content);
        post.addComment(comment);

        commentRepository.save(comment);
        return CommentCreateResult.from(comment);
    }

    public CommentUpdateResult update(Post post, Long commentId, String content, Long loginId) {
        Comment findComment = post.getComments()
                .stream()
                .filter(comment -> comment.getId().equals(commentId))
                .findFirst()
                .orElseThrow(() -> new CommentException(ErrorCode.COMMENT_NOT_FOUND));

        checkAuthor(findComment, loginId);

        findComment.updateContent(content);
        return CommentUpdateResult.from(findComment);
    }

    public void delete(Post post, Long commentId, Long loginId) {
        Comment findComment = post.getComments()
                .stream()
                .filter(comment -> comment.getId().equals(commentId))
                .findFirst()
                .orElseThrow(() -> new CommentException(ErrorCode.COMMENT_NOT_FOUND));

        checkAuthor(findComment, loginId);

        findComment.delete();
    }

    private void checkAuthor(Comment comment, Long loginId) {
        if (!Objects.equals(comment.getAuthorId(), loginId)) {
            throw new PostException(ErrorCode.ACCESS_DENIED);
        }
    }
}
