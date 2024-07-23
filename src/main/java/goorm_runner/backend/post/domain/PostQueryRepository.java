package goorm_runner.backend.post.domain;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PostQueryRepository extends JpaRepository<Post, Long> {
}
