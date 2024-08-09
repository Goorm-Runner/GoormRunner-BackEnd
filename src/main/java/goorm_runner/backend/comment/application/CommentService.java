package goorm_runner.backend.comment.application;

import goorm_runner.backend.comment.domain.Comment;
import goorm_runner.backend.comment.domain.CommentRepository;
import goorm_runner.backend.global.ErrorCode;
import goorm_runner.backend.post.application.exception.PostException;
import goorm_runner.backend.post.domain.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;

    public Comment create(Long authorId, Long postId, String content) {
        postRepository.findById(postId)
                .orElseThrow(() -> new PostException(ErrorCode.POST_NOT_FOUND));

        Comment comment = new Comment(authorId, postId, content);
        return commentRepository.save(comment);
    }

}
