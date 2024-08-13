package goorm_runner.backend.post.domain;

import goorm_runner.backend.post.domain.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {
}
