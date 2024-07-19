package goorm_runner.backend.post.domain;

import goorm_runner.backend.common.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Post extends BaseTimeEntity {

    @GeneratedValue
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
    private Short likeCount;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Category category;

    @Builder
    public Post(Long authorId, String title, String content, Short likeCount, Category category) {
        this.authorId = authorId;
        this.title = title;
        this.content = content;
        this.likeCount = likeCount;
        this.category = category;
    }

    public void update(String title, String content) {
        this.title = title;
        this.content = content;
    }
}
