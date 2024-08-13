package goorm_runner.backend.market.domain;

import goorm_runner.backend.common.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLRestriction;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLRestriction("deleted_at IS NULL")
public class Market extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "market_id", nullable = false)
    private Long id;

    @Column(name = "member_id", nullable = false)
    private Long memberId;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private Integer price;

    @Column(length = 50, nullable = false)
    @Enumerated(EnumType.STRING)
    private MarketCategory category;

    @Column(length = 50, nullable = false)
    @Enumerated(EnumType.STRING)
    private MarketStatus status;

    @Column(length = 50, nullable = false)
    private Integer delivery;

    private int likeCount;

    @Column(length = 300, nullable = false)
    private String imageUrl;

    @OneToMany(mappedBy = "market", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MarketComment> comments = new ArrayList<>();

    private LocalDateTime deletedAt;

    @Builder
    public Market(Long memberId, String title, String content, Integer price,  MarketCategory category, MarketStatus status, Integer delivery, int likeCount, String imageUrl) {
        this.memberId = memberId;
        this.title = title;
        this.content = content;
        this.price = price;
        this.category = category;
        this.status = status;
        this.delivery = delivery;
        this.likeCount = likeCount;
        this.imageUrl = imageUrl;
    }

    public void update(String title, String content, Integer price, MarketCategory category, MarketStatus status, Integer delivery, String imageUrl) {
        this.title = title;
        this.content = content;
        this.price = price;
        this.category = category;
        this.status = status;
        this.delivery = delivery;
        this.imageUrl = imageUrl;
    }

    public void setLikeCount(int likeCount) {
        this.likeCount = likeCount;
    }

    public void delete() {
        deletedAt = LocalDateTime.now();
    }

    public boolean isDeleted() {
        return this.deletedAt != null;
    }
}
