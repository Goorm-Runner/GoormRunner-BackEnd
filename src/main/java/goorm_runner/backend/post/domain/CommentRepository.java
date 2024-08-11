package goorm_runner.backend.post.domain;

import goorm_runner.backend.post.domain.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
}
