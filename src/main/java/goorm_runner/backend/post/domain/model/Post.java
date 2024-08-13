package goorm_runner.backend.post.domain.model;

import goorm_runner.backend.common.BaseTimeEntity;
import goorm_runner.backend.post.domain.exception.PostException;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLRestriction;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;

import static goorm_runner.backend.global.ErrorCode.EMPTY_CONTENT;
import static goorm_runner.backend.global.ErrorCode.EMPTY_TITLE;

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
        validateTitleAndContent(title, content);
        this.authorId = authorId;
        this.title = title;
        this.content = content;
        this.category = category;
    }

    public void update(String title, String content) {
        validateTitleAndContent(title, content);
        this.title = title;
        this.content = content;
    }

    public void delete() {
        deletedAt = LocalDateTime.now();
    }

    public void addComment(Comment comment) {
        this.comments.add(comment);
    }

    private void validateTitleAndContent(String title, String content) {
        if (!StringUtils.hasText(title)) {
            throw new PostException(EMPTY_TITLE);
        }

        if (!StringUtils.hasText(content)) {
            throw new PostException(EMPTY_CONTENT);
        }
    }
}
