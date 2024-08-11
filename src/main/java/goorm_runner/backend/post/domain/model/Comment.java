package goorm_runner.backend.post.domain.model;

import goorm_runner.backend.common.BaseTimeEntity;
import goorm_runner.backend.global.ErrorCode;
import goorm_runner.backend.post.domain.exception.CommentException;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLRestriction;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;

@Entity
@Getter
@SQLRestriction("deleted_at IS NULL")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Comment extends BaseTimeEntity {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "comment_id", nullable = false)
    private Long id;

    @Column(nullable = false)
    private Long authorId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    @Column(nullable = false)
    private String content;

    private LocalDateTime deletedAt;

    protected Comment(Post post, Long authorId, String content) {
        validateNotEmptyContent(content);
        this.post = post;
        this.authorId = authorId;
        this.content = content;
    }

    public void updateContent(String content) {
        validateNotEmptyContent(content);
        this.content = content;
    }

    public void delete() {
        deletedAt = LocalDateTime.now();
    }

    private void validateNotEmptyContent(String content) {
        if (!StringUtils.hasText(content)) {
            throw new CommentException(ErrorCode.EMPTY_CONTENT);
        }
    }
}
