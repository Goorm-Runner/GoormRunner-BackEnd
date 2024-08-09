package goorm_runner.backend.comment.domain;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentReadRepository extends JpaRepository<Comment, Long> {
}
