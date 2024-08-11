package goorm_runner.backend.post.domain.model;

import goorm_runner.backend.common.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLRestriction;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;

@Entity
@Getter
@SQLRestriction("deleted_at IS NULL")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Post extends BaseTimeEntity {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "post_id", nullable = false)
    private Long id;

    @Column(nullable = false)
    private Long authorId;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Category category;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
    private final List<Comment> comments = new LinkedList<>();

    private LocalDateTime deletedAt;

    @Builder
    public Post(Long authorId, String title, String content, Category category) {
        this.authorId = authorId;
        this.title = title;
        this.content = content;
        this.category = category;
    }

    public void update(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public void delete() {
        deletedAt = LocalDateTime.now();
    }

    public Comment addComment(Long authorId, String content) {
        Comment comment = new Comment(this, authorId, content);
        comments.add(comment);
        return comment;
    }
}
