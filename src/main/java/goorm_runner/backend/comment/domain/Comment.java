package goorm_runner.backend.comment.domain;

import goorm_runner.backend.comment.domain.exception.CommentException;
import goorm_runner.backend.common.BaseTimeEntity;
import goorm_runner.backend.global.ErrorCode;
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

    @Column(nullable = false)
    private Long postId;

    @Column(nullable = false)
    private String content;

    private LocalDateTime deletedAt;

    public Comment(Long authorId, Long postId, String content) {
        validateNotEmptyContent(content);
        this.authorId = authorId;
        this.postId = postId;
        this.content = content;
    }

    private void validateNotEmptyContent(String content) {
        if (!StringUtils.hasText(content)) {
            throw new CommentException(ErrorCode.EMPTY_CONTENT);
        }
    }

    public void updateContent(String content) {
        this.content = content;
    }

    public void delete() {
        deletedAt = LocalDateTime.now();
    }
}
