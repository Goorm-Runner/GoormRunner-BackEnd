package goorm_runner.backend.postlike.domain;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PostLikeRepository extends JpaRepository<PostLike, Long> {
    int countByPostId(Long postId);
}
