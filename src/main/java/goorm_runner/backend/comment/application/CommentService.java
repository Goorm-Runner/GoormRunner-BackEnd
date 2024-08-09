package goorm_runner.backend.comment.application;

import goorm_runner.backend.comment.domain.Comment;
import goorm_runner.backend.comment.domain.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;

    public Comment create(Long authorId, Long postId, String content) {
        Comment comment = new Comment(authorId, postId, content);
        return commentRepository.save(comment);
    }
}
