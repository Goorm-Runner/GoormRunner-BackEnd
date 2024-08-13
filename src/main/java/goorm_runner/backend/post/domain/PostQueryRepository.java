package goorm_runner.backend.post.domain;


import goorm_runner.backend.post.domain.model.Category;
import goorm_runner.backend.post.domain.model.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostQueryRepository extends JpaRepository<Post, Long> {
    Page<Post> findByCategory(Category category, Pageable pageable);
}
